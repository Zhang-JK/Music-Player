import hashlib
import json
from http.cookiejar import LWPCookieJar

import requests

host = 'http://127.0.0.1:8080/api/'
headers = {
    'Accept': '*/*',
    'Content-Type': 'application/json',
    'Accept-Encoding': 'gzip, deflate, br',
    'Accept-Language': 'zh-CN,zh;q=0.9,en;q=0.8',
    'Connection': 'keep-alive',
    'host': 'hk.sz.gov.cn:8118',
    'User-agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/98.0.4758.102 Safari/537.36'
}


def login():
    session = requests.session()
    session.cookies = LWPCookieJar(filename='cookies.cookie')

    login = session.post(host+'login', headers=headers, data=
        json.dumps({'username': 'jk', 'password': hashlib.md5('123456'.encode()).hexdigest()})
    ).json()
    print(login)

    session.cookies.save(ignore_discard=True, ignore_expires=True)


if __name__ == '__main__':
    login()
