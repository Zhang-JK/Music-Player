# import time
from datetime import datetime
from http.cookiejar import LWPCookieJar
# import vlc
import requests

link = "http://localhost:3000"


def get_all_list():
    session = requests.session()
    load_cookiejar = LWPCookieJar()
    load_cookiejar.load('wyy-cookies.cookie', ignore_discard=True, ignore_expires=True)
    load_cookies = requests.utils.dict_from_cookiejar(load_cookiejar)
    session.cookies = requests.utils.cookiejar_from_dict(load_cookies)
    info = session.get(link + '/user/account', params={'timestamp': datetime.now().timestamp()}).json()
    song_lists = session.get(link + '/user/playlist', params={'uid': info['account']['id'], 'limit': 3, 'timestamp': datetime.now().timestamp()}).json()
    print(song_lists['playlist'][0]['id'])
    songs = session.get(link + '/playlist/track/all', params={'id': song_lists['playlist'][0]['id'], 'limit': 20, 'timestamp': datetime.now().timestamp()}).json()
    print(songs['songs'][3]['name'], songs['songs'][3]['id'])
    song_url = session.get(link + '/song/url', params={'id': songs['songs'][3]['id'], 'timestamp': datetime.now().timestamp()}).json()
    print(song_url['data'][0]['url'])
    # p = vlc.MediaPlayer(song_url['data'][0]['url'])
    # p.play()
    # time.sleep(2)
    # while p.is_playing():
    #     time.sleep(2)


if __name__ == '__main__':
    get_all_list()
