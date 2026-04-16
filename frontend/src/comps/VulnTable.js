import { useEffect, useState } from 'react';

const VULNS = [
  { id: 'v1_idor', n: 'Broken Access Control (IDOR)', w: 2, desc: 'Измени параметр dir=1 у чужого кролика' },
  { id: 'v2_sqli', n: 'SQL Injection', w: 9, desc: 'Извлеки пароль администратора из базы данных' },
  { id: 'v3_jwt', n: 'Stored XSS', w: 4, desc: 'Выполни alert("XSS")' },
  { id: 'v4_path', n: 'Path Traversal', w: 2, desc: 'Прочитай файл /etc/passwd' },
  { id: 'v5_xxe', n: 'XML External Entity', w: 2, desc: 'Прочитай файл /etc/passwd через XML' },
  { id: 'v6_ssrf', n: 'Server-Side Request Forgery', w: 6, desc: 'Получи доступ к внутреннему эндпоинту /internal/secret' },
  { id: 'v7_deser', n: 'Insecure Deserialization', w: 8, desc: 'Получи содержимое /etc/passwd' },
  { id: 'v8_log', n: 'Log Injection', w: 2, desc: 'Прочитай /etc/passwd через JNDI инъекцию' },
  { id: 'v9_race', n: 'Race Condition', w: 2, desc: 'Добейся двойного настроения happy у кролика' },
  { id: 'v10_back', n: 'Prototype Pollution', w: 3, desc: 'Загрязни прототип в JSON' }
];

const VulnTable = ({ scData }) => {
  const [ws, setWs] = useState({});

  useEffect(() => {
    if (scData?.ws) setWs(scData.ws);
  }, [scData]);

  if (!scData) return <div style={{color:'#888', textAlign:'center', padding:40}}>Syncing progress...</div>;

  return (
    <div style={{background:'#111827', borderRadius:16, padding:24, width:'100%'}}>
      <div style={{display:'flex', justifyContent:'space-between', marginBottom:16}}>
        <h3 style={{margin:0, color:'#fff'}}>Progress</h3>
        <span style={{color:'#10b981', fontWeight:'bold'}}>Grade: {scData.gr} | Score: {scData.sc}/{scData.mx}</span>
      </div>
      <table style={{width:'100%', borderCollapse:'collapse', color:'#d1d5db', fontSize:14}}>
        <thead>
          <tr style={{borderBottom:'1px solid #374151', textAlign:'left'}}>
            <th style={{padding:'8px 0'}}>Module</th>
            <th style={{padding:'8px 0'}}>Weight</th>
            <th style={{padding:'8px 0'}}>Status</th>
           </tr>
        </thead>
        <tbody>
          {VULNS.map(v => {
            const isSolved = ws[v.id] === true;
            return (
              <tr key={v.id} style={{borderBottom:'1px solid #1f2937', background: isSolved ? 'rgba(16,185,129,0.1)' : 'transparent'}}>
                <td style={{padding:'10px 0'}}>
                  <div style={{fontWeight:500}}>{v.n}</div>
                  <div style={{fontSize:11, color:'#6b7280', marginTop:2}}>{v.desc}</div>
                </td>
                <td style={{padding:'10px 0'}}>{v.w}</td>
                <td style={{padding:'10px 0'}}>
                  {isSolved ? <span style={{color:'#10b981'}}>✓ SOLVED</span> : <span style={{color:'#6b7280'}}>🔒 LOCKED</span>}
                </td>
              </tr>
            );
          })}
        </tbody>
      </table>
    </div>
  );
};

export default VulnTable;