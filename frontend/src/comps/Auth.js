import { useState } from 'react';

const Auth = ({ onAuth, onFail }) => {
  const [u, sU] = useState('');
  const [p, sP] = useState('');
  const [t, sT] = useState('login');

  const x = async () => {
    const ok = await onAuth(t, u, p);
    if (!ok) onFail();
  };

  return (
    <div style={{
      background: 'linear-gradient(135deg, #0a0f1e 0%, #0f1629 100%)',
      border: '1px solid #00ffcc',
      borderRadius: '20px',
      padding: '40px',
      width: '380px',
      boxShadow: '0 0 30px rgba(0, 255, 204, 0.2), inset 0 0 20px rgba(0, 255, 204, 0.05)',
      backdropFilter: 'blur(10px)',
      textAlign: 'center'
    }}>
      <div style={{ marginBottom: '30px' }}>
        <h1 style={{
          margin: 0,
          fontSize: '28px',
          fontWeight: 'bold',
          background: 'linear-gradient(135deg, #00ffcc, #00aaff)',
          WebkitBackgroundClip: 'text',
          WebkitTextFillColor: 'transparent',
          letterSpacing: '2px'
        }}>
          BROKEN RABBIT
        </h1>
        <div style={{
          fontSize: '12px',
          color: '#00ffcc',
          marginTop: '5px',
          fontFamily: 'monospace',
          opacity: 0.8
        }}>
          [ CYBERPUNK EDITION v0.9.2 ]
        </div>
      </div>

      <input
        className="inp"
        placeholder="> USERNAME"
        value={u}
        onChange={e => sU(e.target.value)}
        style={{
          width: '100%',
          background: 'rgba(0, 20, 30, 0.8)',
          border: '1px solid #00ffcc',
          color: '#00ffcc',
          padding: '12px',
          marginBottom: '15px',
          borderRadius: '8px',
          fontSize: '14px',
          fontFamily: 'monospace',
          outline: 'none',
          transition: 'all 0.3s'
        }}
        onFocus={e => e.target.style.boxShadow = '0 0 10px rgba(0, 255, 204, 0.5)'}
        onBlur={e => e.target.style.boxShadow = 'none'}
      />

      <input
        className="inp"
        type="password"
        placeholder="> PASSWORD"
        value={p}
        onChange={e => sP(e.target.value)}
        style={{
          width: '100%',
          background: 'rgba(0, 20, 30, 0.8)',
          border: '1px solid #00ffcc',
          color: '#00ffcc',
          padding: '12px',
          marginBottom: '20px',
          borderRadius: '8px',
          fontSize: '14px',
          fontFamily: 'monospace',
          outline: 'none',
          transition: 'all 0.3s'
        }}
        onFocus={e => e.target.style.boxShadow = '0 0 10px rgba(0, 255, 204, 0.5)'}
        onBlur={e => e.target.style.boxShadow = 'none'}
      />

      <button
        className="btn"
        onClick={x}
        style={{
          width: '100%',
          background: 'linear-gradient(90deg, #00ffcc, #00aaff)',
          border: 'none',
          color: '#0a0f1e',
          padding: '12px',
          borderRadius: '8px',
          fontSize: '16px',
          fontWeight: 'bold',
          cursor: 'pointer',
          fontFamily: 'monospace',
          transition: 'all 0.3s',
          textTransform: 'uppercase',
          letterSpacing: '2px'
        }}
        onMouseOver={e => {
          e.target.style.transform = 'scale(1.02)';
          e.target.style.boxShadow = '0 0 20px rgba(0, 255, 204, 0.6)';
        }}
        onMouseOut={e => {
          e.target.style.transform = 'scale(1)';
          e.target.style.boxShadow = 'none';
        }}
      >
        {t === 'login' ? '⚡ ACCESS ⚡' : '🔧 CREATE 🔧'}
      </button>

      <p
        className="lnk"
        onClick={() => sT(t === 'login' ? 'reg' : 'login')}
        style={{
          marginTop: '20px',
          color: '#00ffcc',
          fontSize: '12px',
          cursor: 'pointer',
          fontFamily: 'monospace',
          opacity: 0.7,
          transition: 'opacity 0.3s',
          textDecoration: 'none'
        }}
        onMouseOver={e => e.target.style.opacity = '1'}
        onMouseOut={e => e.target.style.opacity = '0.7'}
      >
        {t === 'login' ? '[ NO ACCOUNT? ]' : '[ HAVE ONE? ]'}
      </p>
    </div>
  );
};

export default Auth;