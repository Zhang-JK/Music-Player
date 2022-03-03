import time
from datetime import datetime
from http.cookiejar import LWPCookieJar
import vlc
import requests


def get_all_list():
    session = requests.session()
    load_cookiejar = LWPCookieJar()
    load_cookiejar.load('wyy-cookies.cookie', ignore_discard=True, ignore_expires=True)
    load_cookies = requests.utils.dict_from_cookiejar(load_cookiejar)
    session.cookies = requests.utils.cookiejar_from_dict(load_cookies)
    info = session.get('http://localhost:3000/user/account', params={'timestamp': datetime.now().timestamp()}).json()
    song_lists = session.get('http://localhost:3000/user/playlist', params={'uid': info['account']['id'], 'limit': 3, 'timestamp': datetime.now().timestamp()}).json()
    print(song_lists['playlist'][0]['id'])
    songs = session.get('http://localhost:3000/playlist/track/all', params={'id': song_lists['playlist'][0]['id'], 'limit': 10, 'timestamp': datetime.now().timestamp()}).json()
    print(songs['songs'][6]['name'], songs['songs'][6]['id'])
    song_url = session.get('http://localhost:3000/song/url', params={'id': songs['songs'][6]['id'], 'br': 320000, 'timestamp': datetime.now().timestamp()}).json()
    print(song_url['data'][0]['url'])
    p = vlc.MediaPlayer(song_url['data'][0]['url'])
    p.play()
    time.sleep(2)
    while p.is_playing():
        time.sleep(2)


if __name__ == '__main__':
    get_all_list()
