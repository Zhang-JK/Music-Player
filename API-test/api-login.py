import hashlib
import json
import time
from http.cookiejar import LWPCookieJar

import qrcode
import requests

host = 'http://127.0.0.1:8080/api'
headers = {
    'Accept': '*/*',
    'Content-Type': 'application/json',
    'Accept-Encoding': 'gzip, deflate, br',
    'Accept-Language': 'zh-CN,zh;q=0.9,en;q=0.8',
    'Connection': 'keep-alive',
    'User-agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/98.0.4758.102 Safari/537.36'
}


def login():
    session = requests.session()
    session.cookies = LWPCookieJar(filename='cookies.cookie')

    login = session.post(host + '/login', headers=headers, data=
    json.dumps({'username': 'jk', 'password': hashlib.md5('123456'.encode()).hexdigest()})
                         ).json()
    print(login)

    session.cookies.save(ignore_discard=True, ignore_expires=True)


def bili_login():
    session = requests.session()
    load_cookiejar = LWPCookieJar()
    load_cookiejar.load('cookies.cookie', ignore_discard=True, ignore_expires=True)
    session.cookies = load_cookiejar

    bili_login = session.post(host + '/bili/qrcode', headers=headers).json()
    print(bili_login)
    loginurl = bili_login['data']['url']
    oauthKey = bili_login['data']['oauthKey']

    qr = qrcode.QRCode()
    qr.add_data(loginurl)
    img = qr.make_image()
    img.save('./bili-login-qrcode.png')

    while 1:
        qrcodedata = session.post(host + '/bili/qr-check', data={'oauthKey': oauthKey}).json()
        time.sleep(3)
        print(qrcodedata['message'])
        if qrcodedata['code'] == 200:
            break


if __name__ == '__main__':
    login()
    time.sleep(2)
    bili_login()