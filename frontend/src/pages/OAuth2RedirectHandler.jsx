import React, { useEffect } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { Sparkles } from 'lucide-react';

const OAuth2RedirectHandler = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const { handleOAuth2Success } = useAuth();

  useEffect(() => {
    const params = new URLSearchParams(location.search);
    const token = params.get('token');
    const role = params.get('role') || 'CANDIDATE';
    const userId = params.get('userId');
    const name = params.get('name');

    if (token && userId) {
      const decodedName = name ? decodeURIComponent(name) : 'OAuth User';
      const user = handleOAuth2Success(token, role, userId, decodedName);

      if (user.role === 'RECRUITER') {
        navigate('/recruiter/dashboard', { replace: true });
      } else {
        navigate('/candidate/dashboard', { replace: true });
      }
    } else {
      navigate('/login', { replace: true });
    }
  }, [location, navigate, handleOAuth2Success]);

  return (
    <div style={{ minHeight: '60vh', display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center', textAlign: 'center' }}>
      <div style={{ 
        width: '56px', 
        height: '56px', 
        borderRadius: '16px', 
        background: 'linear-gradient(135deg, #06b6d4 0%, #3b82f6 100%)', 
        display: 'flex', 
        alignItems: 'center', 
        justifyContent: 'center', 
        marginBottom: '20px',
        boxShadow: '0 0 25px rgba(6, 182, 212, 0.4)'
      }} className="pulse-glow">
        <Sparkles size={28} color="white" />
      </div>
      <h3 style={{ fontSize: '1.25rem', marginBottom: '8px' }}>Authenticating via OAuth2...</h3>
      <p style={{ color: 'var(--text-secondary)', fontSize: '0.85rem' }}>Setting up your secure session token. Please wait.</p>
    </div>
  );
};

export default OAuth2RedirectHandler;
