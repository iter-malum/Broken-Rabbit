import { useRabbitState } from './hooks/useRabbitState';
import Auth from './comps/Auth';
import Dashboard from './comps/Dashboard';


function App() {
  const st = useRabbitState();
  return st.uid ? (
    <Dashboard 
      uid={st.uid} 
      rb={st.rb} 
      sc={st.sc} 
      err={st.err} 
      sync={st.sync} 
      logout={st.logout} 
    />
  ) : (
    <Auth onAuth={st.hndlAuth} onFail={() => alert('Bad creds or net err')} />
  );
}

export default App;