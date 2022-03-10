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


def bili_fav_list():
    session = requests.session()
    load_cookiejar = LWPCookieJar()
    load_cookiejar.load('cookies.cookie', ignore_discard=True, ignore_expires=True)
    session.cookies = load_cookiejar

    bili_fav_list = session.post(host + '/bili/fav-list', headers=headers).json()
    print(bili_fav_list)

def bili_list_detail():
    session = requests.session()
    load_cookiejar = LWPCookieJar()
    load_cookiejar.load('cookies.cookie', ignore_discard=True, ignore_expires=True)
    session.cookies = load_cookiejar

    detail = session.post(host + '/bili/list-detail', params={'id': 284110317, 'limit': 20}, headers=headers).json()
    print(detail)


def netease_login():
    session = requests.session()
    load_cookiejar = LWPCookieJar()
    load_cookiejar.load('cookies.cookie', ignore_discard=True, ignore_expires=True)
    session.cookies = load_cookiejar

    netease_login = session.post(host + '/netease/qrcode', headers=headers).json()
    print(netease_login)
    loginurl = netease_login['data']['url']
    oauthKey = netease_login['data']['oauthKey']

    qr = qrcode.QRCode()
    qr.add_data(loginurl)
    img = qr.make_image()
    img.save('./wyy-login-qrcode.png')

    while 1:
        qrcodedata = session.post(host + '/netease/qr-check', data={'oauthKey': oauthKey}).json()
        time.sleep(3)
        print(qrcodedata['message'])
        if qrcodedata['code'] == 200:
            break


def netease_login_check():
    session = requests.session()
    load_cookiejar = LWPCookieJar()
    load_cookiejar.load('cookies.cookie', ignore_discard=True, ignore_expires=True)
    session.cookies = load_cookiejar

    netease_login_check = session.post(host + '/netease/login-check', headers=headers).json()
    print(netease_login_check)


def bili_login_check():
    session = requests.session()
    load_cookiejar = LWPCookieJar()
    load_cookiejar.load('cookies.cookie', ignore_discard=True, ignore_expires=True)
    session.cookies = load_cookiejar

    bili_login_check = session.post(host + '/bili/login-check', headers=headers).json()
    print(bili_login_check)


def netease_fav_list():
    session = requests.session()
    load_cookiejar = LWPCookieJar()
    load_cookiejar.load('cookies.cookie', ignore_discard=True, ignore_expires=True)
    session.cookies = load_cookiejar

    netease_fav_list = session.post(host + '/netease/fav-list', headers=headers).json()
    print(netease_fav_list)


def netease_list_detail():
    session = requests.session()
    load_cookiejar = LWPCookieJar()
    load_cookiejar.load('cookies.cookie', ignore_discard=True, ignore_expires=True)
    session.cookies = load_cookiejar

    detail = session.post(host + '/netease/list-detail', params={'id': 26460971, 'offset': 1, 'limit': 10},
                          headers=headers).json()
    print(detail)


def netease_list_import():
    session = requests.session()
    load_cookiejar = LWPCookieJar()
    load_cookiejar.load('cookies.cookie', ignore_discard=True, ignore_expires=True)
    session.cookies = load_cookiejar

    detail = session.post(host + '/song/import-list', params={'listId': 26460971, 'platform': 2},
                          headers=headers).json()
    print(detail)


def netease_id_import():
    session = requests.session()
    load_cookiejar = LWPCookieJar()
    load_cookiejar.load('cookies.cookie', ignore_discard=True, ignore_expires=True)
    session.cookies = load_cookiejar

    detail = session.post(host + '/song/import', params={'id': [183948520200, 1350717910], 'platform': 2},
                          headers=headers).json()
    print(detail)


def bili_id_import():
    session = requests.session()
    load_cookiejar = LWPCookieJar()
    load_cookiejar.load('cookies.cookie', ignore_discard=True, ignore_expires=True)
    session.cookies = load_cookiejar

    detail = session.post(host + '/song/import', params={'id': [8508035, 9966225000], 'platform': 1},
                          headers=headers).json()
    print(detail)


def bili_list_import():
    session = requests.session()
    load_cookiejar = LWPCookieJar()
    load_cookiejar.load('cookies.cookie', ignore_discard=True, ignore_expires=True)
    session.cookies = load_cookiejar

    detail = session.post(host + '/song/import-list', params={'listId': 284110317, 'platform': 1},
                          headers=headers).json()
    print(detail)


def netease_play():
    session = requests.session()
    load_cookiejar = LWPCookieJar()
    load_cookiejar.load('cookies.cookie', ignore_discard=True, ignore_expires=True)
    session.cookies = load_cookiejar

    player = session.post(host + '/player/link', params={'ids': [936622500, 549459490], 'platform': 1},
                          headers=headers).json()
    print(player)


if __name__ == '__main__':
    # login()
    # time.sleep(2)
    # bili_login()
    # bili_fav_list()
    # netease_login()
    # netease_login_check()
    # bili_login_check()
    # netease_fav_list()
    # netease_list_detail()
    # time.sleep(2)
    # netease_list_import()
    # netease_id_import()
    # bili_list_detail()
    # bili_id_import()
    # bili_list_import()
    netease_play()