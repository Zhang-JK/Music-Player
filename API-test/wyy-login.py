import base64
import codecs
from datetime import datetime
import time
from http.cookiejar import LWPCookieJar
from io import BytesIO
from threading import Thread

import agent
import qrcode
from Crypto.Cipher import AES
import requests
from PIL import Image

link = "http://localhost:3000"


class showpng(Thread):
    def __init__(self, data):
        Thread.__init__(self)
        self.data = data

    def run(self):
        img = Image.open(BytesIO(self.data))
        img.show()


def login():
    session = requests.session()
    session.cookies = LWPCookieJar(filename='wyy-cookies.cookie')
    getlogin = session.get(link + '/login/qr/key', params={'timestamp': datetime.now().timestamp()}).json()

    pngurl = 'https://music.163.com/login?codekey=' + getlogin['data']['unikey'] + '&refer=scan'
    qr = qrcode.QRCode()
    qr.add_data(pngurl)
    img = qr.make_image()
    a = BytesIO()
    img.save(a, 'png')
    png = a.getvalue()
    a.close()
    t = showpng(png)
    t.start()

    tokenurl = link + '/login/qr/check'
    while 1:
        qrcodedata = session.get(tokenurl, params={'key': getlogin['data']['unikey'],
                                                   'timestamp': datetime.now().timestamp()}).json()
        if '801' in str(qrcodedata['code']):
            print('二维码未失效，请扫码！')
        elif '802' in str(qrcodedata['code']):
            print('已扫码，请确认！')
        elif '803' in str(qrcodedata['code']):
            print('已确认，登入成功！')
            break
        else:
            print('其他：', qrcodedata)
        time.sleep(2)
    print(qrcodedata)
    session.cookies.save(ignore_discard=True, ignore_expires=True)


if __name__ == '__main__':
    login()
