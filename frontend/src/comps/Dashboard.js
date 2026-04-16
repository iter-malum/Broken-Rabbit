// FILE: frontend/src/comps/Dashboard.js
import { useEffect, useState } from 'react';
import RabbitCanvas from './RabbitCanvas';
import VulnTable from './VulnTable';

const Dashboard = ({ uid, rb, sc, err, sync, logout }) => {
  const [ws, sWs] = useState({});
  const [user, setUser] = useState(null);

  useEffect(() => {
    const i = setInterval(sync, 3000);
    return () => clearInterval(i);
  }, [sync]);

  useEffect(() => {
    if (sc?.ws) sWs(prev => ({...prev, ...sc.ws}));
  }, [sc]);

  useEffect(() => {
    fetch(`/api/auth/user/${uid}`)
      .then(res => res.json())
      .then(data => {
        setUser(data);
      })
      .catch(err => console.error(err));
  }, [uid]);

  useEffect(() => {
    if (user?.name) {
      const originalAlert = window.alert;
      window.alert = function(msg) {
        if (msg === 'XSS') {
          fetch('/api/progress/check?type=xss', { method: 'POST', headers: { 'X-Cid': uid } });
        }
        originalAlert(msg);
      };
    }
  }, [user, uid]);

  if (!user) return <div style={{color: '#00ffcc', textAlign: 'center', padding: '50px', fontFamily: 'monospace'}}>LOADING...</div>;

  return (
    <div style={{
      minHeight: '100vh',
      background: 'linear-gradient(135deg, #0a0f1e 0%, #0f1629 100%)',
      padding: '20px'
    }}>
      <div style={{
        background: 'rgba(10, 15, 30, 0.8)',
        backdropFilter: 'blur(10px)',
        borderBottom: '1px solid #00ffcc',
        padding: '15px 30px',
        marginBottom: '30px',
        display: 'flex',
        justifyContent: 'space-between',
        alignItems: 'center',
        borderRadius: '12px',
        boxShadow: '0 0 20px rgba(0, 255, 204, 0.1)'
      }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: '15px' }}>
          <span style={{
            color: '#00ffcc',
            fontFamily: 'monospace',
            fontSize: '14px',
            background: 'rgba(0, 255, 204, 0.1)',
            padding: '5px 12px',
            borderRadius: '20px',
            border: '1px solid #00ffcc'
          }}>
            ID: {uid}
          </span>
          <span style={{
            color: '#fff',
            fontFamily: 'monospace',
            fontSize: '12px',
            opacity: 0.7
          }}>
            {user.name?.replace(/<[^>]*>/g, '').substring(0, 30)}'s session
          </span>
        </div>
        <button
          onClick={logout}
          style={{
            background: 'rgba(255, 0, 100, 0.2)',
            border: '1px solid #ff0066',
            color: '#ff0066',
            padding: '6px 16px',
            borderRadius: '8px',
            cursor: 'pointer',
            fontFamily: 'monospace',
            fontSize: '12px',
            fontWeight: 'bold',
            transition: 'all 0.3s'
          }}
          onMouseOver={e => {
            e.target.style.background = 'rgba(255, 0, 100, 0.4)';
            e.target.style.boxShadow = '0 0 10px rgba(255, 0, 100, 0.5)';
          }}
          onMouseOut={e => {
            e.target.style.background = 'rgba(255, 0, 100, 0.2)';
            e.target.style.boxShadow = 'none';
          }}
        >
          EXIT
        </button>
      </div>

      <div style={{
        textAlign: 'center',
        marginBottom: '30px',
        padding: '20px',
        background: 'rgba(0, 255, 204, 0.05)',
        borderRadius: '12px',
        borderLeft: '3px solid #00ffcc'
      }}>
        <div style={{
          color: '#00ffcc',
          fontFamily: 'monospace',
          fontSize: '14px',
          letterSpacing: '2px'
        }}>
          WELCOME BACK,
        </div>
        <div style={{
          fontSize: '24px',
          fontWeight: 'bold',
          background: 'linear-gradient(135deg, #00ffcc, #00aaff)',
          WebkitBackgroundClip: 'text',
          WebkitTextFillColor: 'transparent',
          fontFamily: 'monospace',
          marginTop: '5px'
        }}>
          <span dangerouslySetInnerHTML={{ __html: user.name }}></span>
        </div>
      </div>

      <div className="main-grid" style={{
        display: 'grid',
        gridTemplateColumns: '1fr 1.5fr',
        gap: '24px',
        padding: '0 20px'
      }}>
        <div className="rb-panel" style={{
          background: 'rgba(10, 15, 30, 0.6)',
          borderRadius: '20px',
          padding: '20px',
          border: '1px solid rgba(0, 255, 204, 0.2)'
        }}>
          <RabbitCanvas st={rb} />
          {err && <p className="err-msg" style={{
            color: '#ff0066',
            textAlign: 'center',
            marginTop: '15px',
            fontFamily: 'monospace'
          }}>{err}</p>}
        </div>
        <div className="vuln-panel">
          <VulnTable scData={sc} ws={ws} />
        </div>
      </div>
    </div>
  );
};

export default Dashboard;