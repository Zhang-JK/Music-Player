import time
from http.cookiejar import LWPCookieJar
import requests
import qrcode
from io import BytesIO
from PIL import Image
from threading import Thread


class ShowPNG(Thread):
    def __init__(self, data):
        Thread.__init__(self)
        self.data = data

    def run(self):
        img = Image.open(BytesIO(self.data))
        img.show()


def login():
    session = requests.session()
    session.cookies = LWPCookieJar(filename='bili-cookies.cookie')

    getlogin = session.get('https://passport.bilibili.com/qrcode/getLoginUrl').json()
    loginurl = getlogin['data']['url']
    oauthKey = getlogin['data']['oauthKey']

    qr = qrcode.QRCode()
    qr.add_data(loginurl)
    img = qr.make_image()
    img.save('./bili-login-qrcode.png')

    tokenurl = 'https://passport.bilibili.com/qrcode/getLoginInfo'
    while 1:
        qrcodedata = session.post(tokenurl, data={'oauthKey': oauthKey, 'gourl': 'https://www.bilibili.com/'}).json()
        print(qrcodedata)
        if '-4' in str(qrcodedata['data']):
            print('二维码未失效，请扫码！')
        elif '-5' in str(qrcodedata['data']):
            print('已扫码，请确认！')
        elif '-2' in str(qrcodedata['data']):
            print('二维码已失效，请重新运行！')
        elif 'True' in str(qrcodedata['status']):
            print('已确认，登入成功！')
            session.get(qrcodedata['data']['url'])
            break
        else:
            print('其他：', qrcodedata)
        time.sleep(2)
    session.cookies.save(ignore_discard=True, ignore_expires=True)


if __name__ == '__main__':
    login()
