from http.cookiejar import LWPCookieJar

import requests


def get_all_fav():
    session = requests.session()
    load_cookiejar = LWPCookieJar()
    load_cookiejar.load('bili-cookies.cookie', ignore_discard=True, ignore_expires=True)
    load_cookies = requests.utils.dict_from_cookiejar(load_cookiejar)
    session.cookies = requests.utils.cookiejar_from_dict(load_cookies)
    info = session.get('http://api.bilibili.com/x/web-interface/nav').json()
    print(info['data']['mid'])
    params = {'up_mid': info['data']['mid']}
    fav_lists = session.get('http://api.bilibili.com/x/v3/fav/folder/created/list-all', params=params).json()
    if fav_lists['code'] == 0:
        print("get successfully, total number: ", fav_lists['data']['count'])
        print(fav_lists['data']['list'])
    else:
        print("get fav list fail")


if __name__ == '__main__':
    get_all_fav()
