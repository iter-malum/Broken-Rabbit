import { useState, useEffect, useCallback } from 'react';

const API = '/api';

export const useRabbitState = () => {
  const [uid, setUid] = useState(localStorage.getItem('uid'));
  const [tok, setTok] = useState(localStorage.getItem('tok'));
  const [rb, setRb] = useState(null);
  const [sc, setSc] = useState(null);
  const [err, setErr] = useState(null);

  const sync = useCallback(async (forceUid) => {
    try {
      const currentUid = forceUid || uid || localStorage.getItem('uid');
      const currentTok = tok || localStorage.getItem('tok');
      if (!currentUid) return;
      
      const headers = { 'X-Cid': currentUid };
      if (currentTok) {
        headers['Authorization'] = `Bearer ${currentTok}`;
      }
      
      const r = await fetch(`${API}/rabbit/info`, { headers });
      if (!r.ok) throw new Error('auth_fail');
      const d = await r.json();
      setRb(d.rb);
      setSc(d.sc);
      setErr(null);
    } catch (e) { setErr(e.message); }
  }, [uid, tok]);

  useEffect(() => { sync(); }, [sync]);

  const hndlAuth = async (t, un, ph) => {
    try {
      const ep = t === 'reg' ? 'auth/reg' : 'auth/login';
      const r = await fetch(`${API}/${ep}`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ un, ph })
      });
      const j = await r.json();
      if (r.ok && j.t && j.uid) {
        setUid(String(j.uid));
        setTok(j.t);
        localStorage.setItem('uid', j.uid);
        localStorage.setItem('tok', j.t);
        setTimeout(() => sync(String(j.uid)), 300); 
        return true;
      }
      return r.ok;
    } catch (e) { setErr('net_err'); return false; }
  };

  return { uid, rb, sc, err, sync, hndlAuth, logout: () => { localStorage.clear(); window.location.reload(); } };
};