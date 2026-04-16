import { useState, useEffect } from 'react';
import { DotLottieReact } from '@lottiefiles/dotlottie-react';

const RabbitCanvas = ({ st, cfg, score = 0 }) => {
  const [showStory, setShowStory] = useState(false);

  useEffect(() => {
    const hasRead = localStorage.getItem('rabbit_story_read');
    if (!hasRead) {
      setTimeout(() => setShowStory(true), 300);
    }
  }, []);

  const handleCloseStory = () => {
    setShowStory(false);
    localStorage.setItem('rabbit_story_read', 'true');
  };

  return (
    <div style={{
      width: '100%',
      maxWidth: '500px',
      margin: '0 auto',
      background: 'linear-gradient(135deg, #f0f9ff 0%, #e0f2fe 100%)',
      borderRadius: '24px',
      padding: '20px',
      position: 'relative',
      minHeight: '450px',
      display: 'flex',
      flexDirection: 'column',
      alignItems: 'center',
      justifyContent: 'center',
      boxShadow: '0 10px 25px rgba(0,0,0,0.1)'
    }}>
      <DotLottieReact
        src="/assets/Rabit.json"
        loop
        autoplay
        style={{ width: '100%', height: '400px' }}
      />

      {showStory && (
        <div style={{
          position: 'absolute',
          top: '20px',
          left: '20px',
          right: '20px',
          background: 'rgba(255, 255, 255, 0.98)',
          border: '1px solid #cbd5e1',
          borderRadius: '16px',
          padding: '20px',
          zIndex: 10,
          boxShadow: '0 10px 30px rgba(0,0,0,0.2)',
          animation: 'fadeIn 0.5s ease'
        }}>
          <p style={{ color: '#1e293b', fontSize: '15px', lineHeight: '1.6', margin: '0 0 15px 0' }}>
            🐰 <strong>Привет, исследователь!</strong><br/><br/>
            Меня зовут Багз. Я — кибер-кролик, который потерял свои цифровые уши. 
            В моём коде спрятаны 10 уязвимостей. Хакеры уже нашли их, но только ты можешь помочь мне выяснить, как их исправить.
            <br/><br/>
            Найди каждую брешь, используй её и помоги мне восстановить защиту. 
            Каждая исправленная уязвимость приближает нас к цели — собрать все флаги и стать неуязвимым!
            <br/><br/>
            <span style={{ color: '#64748b', fontSize: '13px' }}>Готов испытать свои навыки?</span>
          </p>
          <button
            onClick={handleCloseStory}
            style={{
              background: '#10b981',
              color: '#fff',
              border: 'none',
              padding: '10px 20px',
              borderRadius: '10px',
              cursor: 'pointer',
              fontSize: '15px',
              fontWeight: '600',
              transition: 'background 0.2s'
            }}
            onMouseOver={e => e.target.style.background = '#059669'}
            onMouseOut={e => e.target.style.background = '#10b981'}
          >
            Начинаю поиск 🔍
          </button>
        </div>
      )}

      <style>{`
        @keyframes fadeIn {
          from { opacity: 0; transform: translateY(-10px); }
          to { opacity: 1; transform: translateY(0); }
        }
      `}</style>
    </div>
  );
};

export default RabbitCanvas;