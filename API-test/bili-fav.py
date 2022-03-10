import time
from http.cookiejar import LWPCookieJar

import requests
import vlc

headers = {
    'Accept': '*/*',
    'Connection': 'keep-alive',
    'Refer': 'www.bilibili.com',
    'User-agent': 'Mozilla/5.0 BiliDroid/6.63.0 (bbcallen@gmail.com)',
    'referer': 'www.bilibili.com'
}


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
    play_url = session.get('http://api.bilibili.com/x/player/playurl', params={'avid': 758817007, 'cid': 358389121, 'fnval': 80}).json()
    print(play_url)
    print(play_url['data']['dash']['audio'][0]['baseUrl'])
    music = requests.get(play_url['data']['dash']['audio'][0]['baseUrl'], headers=headers)
    print(music)
    # p = vlc.MediaPlayer(play_url['data']['dash']['audio'][0]['baseUrl'])
    # p.play()
    # time.sleep(2)
    # while p.is_playing():
    #     time.sleep(2)


if __name__ == '__main__':
    get_all_fav()
