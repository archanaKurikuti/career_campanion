import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { 
  Briefcase, 
  FileText, 
  Sparkles, 
  Bell, 
  User, 
  LogOut, 
  Building2, 
  Layers,
  ChevronDown,
  Clock,
  Menu,
  X
} from 'lucide-react';

const Navbar = () => {
  const { user, role, logout, notifications, unreadCount, markNotificationRead } = useAuth();
  const [showNotifications, setShowNotifications] = useState(false);
  const [showProfileMenu, setShowProfileMenu] = useState(false);
  const [mobileOpen, setMobileOpen] = useState(false);
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  const linkStyle = {
    display: 'flex',
    alignItems: 'center',
    gap: '6px',
    fontSize: '0.875rem',
    fontWeight: 500,
    color: 'var(--text-secondary)',
    padding: '6px 12px',
    borderRadius: 'var(--radius-sm)',
    transition: 'var(--transition)',
  };

  return (
    <header style={{
      background: 'var(--bg-primary)',
      borderBottom: '1px solid var(--border)',
      position: 'sticky',
      top: 0,
      zIndex: 100,
    }}>
      <div style={{ maxWidth: '1200px', margin: '0 auto', padding: '0 24px', height: '56px', display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>

        {/* Brand */}
        <Link to="/" style={{ display: 'flex', alignItems: 'center', gap: '8px', textDecoration: 'none' }}>
          <Briefcase size={20} color="var(--accent)" />
          <span style={{ fontSize: '1.1rem', fontWeight: 700, color: 'var(--text-primary)' }}>
            Career Companion
          </span>
        </Link>

        {/* Navigation Links */}
        <nav style={{ display: 'flex', alignItems: 'center', gap: '4px' }}>
          <Link to="/jobs" style={linkStyle}>
            <Briefcase size={15} /> Jobs
          </Link>

          {user && role === 'CANDIDATE' && (
            <>
              <Link to="/candidate/dashboard" style={linkStyle}>
                <Layers size={15} /> Dashboard
              </Link>
              <Link to="/resumes" style={linkStyle}>
                <FileText size={15} /> Resume
              </Link>
              <Link to="/applications" style={linkStyle}>
                <Clock size={15} /> Applications
              </Link>
              <Link to="/ai-assistant" style={linkStyle}>
                <Sparkles size={15} /> AI Assistant
              </Link>
            </>
          )}

          {user && role === 'RECRUITER' && (
            <>
              <Link to="/recruiter/dashboard" style={linkStyle}>
                <Layers size={15} /> Dashboard
              </Link>
              <Link to="/recruiter/company" style={linkStyle}>
                <Building2 size={15} /> Company
              </Link>
              <Link to="/recruiter/post-job" style={linkStyle}>
                <Briefcase size={15} /> Post Job
              </Link>
              <Link to="/recruiter/applicants" style={linkStyle}>
                <User size={15} /> Applicants
              </Link>
            </>
          )}
        </nav>

        {/* Right Actions */}
        <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
          {user ? (
            <>
              {/* Notifications */}
              <div style={{ position: 'relative' }}>
                <button
                  onClick={() => { setShowNotifications(!showNotifications); setShowProfileMenu(false); }}
                  style={{
                    background: 'transparent',
                    border: '1px solid var(--border)',
                    borderRadius: '50%',
                    width: '36px',
                    height: '36px',
                    display: 'flex',
                    alignItems: 'center',
                    justifyContent: 'center',
                    color: 'var(--text-secondary)',
                    position: 'relative',
                  }}
                >
                  <Bell size={16} />
                  {unreadCount > 0 && (
                    <span style={{
                      position: 'absolute',
                      top: '-2px',
                      right: '-2px',
                      background: 'var(--danger)',
                      color: 'white',
                      fontSize: '0.6rem',
                      fontWeight: 700,
                      borderRadius: '10px',
                      padding: '1px 5px',
                      minWidth: '16px',
                      textAlign: 'center',
                    }}>
                      {unreadCount}
                    </span>
                  )}
                </button>

                {showNotifications && (
                  <div className="card" style={{
                    position: 'absolute',
                    right: 0,
                    top: '44px',
                    width: '300px',
                    maxHeight: '360px',
                    overflowY: 'auto',
                    padding: '12px',
                    zIndex: 200,
                    boxShadow: 'var(--shadow-lg)',
                  }}>
                    <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '8px', paddingBottom: '8px', borderBottom: '1px solid var(--border)' }}>
                      <span style={{ fontSize: '0.875rem', fontWeight: 600 }}>Notifications</span>
                      <span style={{ fontSize: '0.75rem', color: 'var(--text-muted)' }}>{notifications.length}</span>
                    </div>

                    {notifications.length === 0 ? (
                      <p style={{ fontSize: '0.8rem', color: 'var(--text-muted)', textAlign: 'center', padding: '16px 0' }}>
                        No notifications yet.
                      </p>
                    ) : (
                      notifications.map(n => (
                        <div
                          key={n.id}
                          onClick={() => markNotificationRead(n.id)}
                          style={{
                            padding: '8px',
                            borderRadius: 'var(--radius-sm)',
                            background: n.read ? 'transparent' : 'var(--accent-light)',
                            marginBottom: '4px',
                            cursor: 'pointer',
                          }}
                        >
                          <p style={{ fontSize: '0.8rem', fontWeight: n.read ? 400 : 600, color: 'var(--text-primary)' }}>{n.message}</p>
                          <span style={{ fontSize: '0.7rem', color: 'var(--text-muted)' }}>
                            {n.createdAt ? new Date(n.createdAt).toLocaleDateString() : 'Just now'}
                          </span>
                        </div>
                      ))
                    )}
                  </div>
                )}
              </div>

              {/* Profile */}
              <div style={{ position: 'relative' }}>
                <button
                  onClick={() => { setShowProfileMenu(!showProfileMenu); setShowNotifications(false); }}
                  style={{
                    display: 'flex',
                    alignItems: 'center',
                    gap: '8px',
                    background: 'transparent',
                    border: '1px solid var(--border)',
                    padding: '4px 12px 4px 4px',
                    borderRadius: '20px',
                  }}
                >
                  <div style={{
                    width: '28px',
                    height: '28px',
                    borderRadius: '50%',
                    background: 'var(--accent)',
                    display: 'flex',
                    alignItems: 'center',
                    justifyContent: 'center',
                    fontWeight: 600,
                    fontSize: '0.75rem',
                    color: 'white',
                  }}>
                    {user?.name ? user.name.charAt(0).toUpperCase() : 'U'}
                  </div>
                  <span style={{ fontSize: '0.8rem', fontWeight: 600, color: 'var(--text-primary)' }}>
                    {user?.name || 'User'}
                  </span>
                  <ChevronDown size={14} color="var(--text-muted)" />
                </button>

                {showProfileMenu && (
                  <div className="card" style={{
                    position: 'absolute',
                    right: 0,
                    top: '44px',
                    width: '160px',
                    padding: '4px',
                    zIndex: 200,
                    boxShadow: 'var(--shadow-lg)',
                  }}>
                    <div style={{ padding: '8px 12px', borderBottom: '1px solid var(--border)', marginBottom: '4px' }}>
                      <span style={{ fontSize: '0.7rem', fontWeight: 600, color: 'var(--text-muted)', textTransform: 'uppercase' }}>{role}</span>
                    </div>
                    <button
                      onClick={handleLogout}
                      style={{
                        width: '100%',
                        display: 'flex',
                        alignItems: 'center',
                        gap: '8px',
                        padding: '8px 12px',
                        borderRadius: 'var(--radius-sm)',
                        background: 'transparent',
                        color: 'var(--danger)',
                        fontSize: '0.8rem',
                        fontWeight: 600,
                      }}
                    >
                      <LogOut size={15} /> Log Out
                    </button>
                  </div>
                )}
              </div>
            </>
          ) : (
            <>
              <Link to="/login" className="btn-secondary" style={{ fontSize: '0.8rem', padding: '6px 16px' }}>
                Log In
              </Link>
              <Link to="/register" className="btn-primary" style={{ fontSize: '0.8rem', padding: '6px 16px' }}>
                Register
              </Link>
            </>
          )}
        </div>

      </div>
    </header>
  );
};

export default Navbar;
