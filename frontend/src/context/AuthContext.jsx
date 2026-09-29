import React, { createContext, useContext, useState, useEffect } from 'react';
import api from '../api/axiosConfig';

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [token, setToken] = useState(localStorage.getItem('token') || null);
  const [user, setUser] = useState(() => {
    const saved = localStorage.getItem('user');
    return saved ? JSON.parse(saved) : null;
  });
  const [loading, setLoading] = useState(true);
  const [notifications, setNotifications] = useState([]);
  const [unreadCount, setUnreadCount] = useState(0);

  useEffect(() => {
    if (token) {
      fetchUserProfile();
    } else {
      setLoading(false);
    }
  }, [token]);

  const fetchUserProfile = async () => {
    try {
      if (!user || !user.id || !user.role) {
        setLoading(false);
        return;
      }
      const endpoint = user.role === 'RECRUITER' ? `/api/recruiters/${user.id}` : `/api/candidates/${user.id}`;
      const res = await api.get(endpoint);
      const updated = { ...user, ...res.data };
      setUser(updated);
      localStorage.setItem('user', JSON.stringify(updated));

      // Fetch user notifications
      fetchNotifications(user.id);
    } catch (err) {
      console.error('Failed to fetch user profile:', err);
    } finally {
      setLoading(false);
    }
  };

  const fetchNotifications = async (userId) => {
    try {
      const res = await api.get(`/api/notifications/user/${userId}`);
      setNotifications(res.data || []);
      setUnreadCount((res.data || []).filter(n => !n.read).length);
    } catch (err) {
      console.error('Failed to fetch notifications:', err);
    }
  };

  const login = async (email, password) => {
    const response = await api.post('/api/auth/login', { email, password });
    const { token, role, userId, name } = response.data;
    
    localStorage.setItem('token', token);
    setToken(token);

    const userData = { id: userId, role, name, email };
    localStorage.setItem('user', JSON.stringify(userData));
    setUser(userData);

    fetchNotifications(userId);
    return userData;
  };

  const register = async (registerData) => {
    const response = await api.post('/api/auth/register', registerData);
    const { token, role, userId, name } = response.data;

    localStorage.setItem('token', token);
    setToken(token);

    const userData = { id: userId, role, name, email: registerData.email };
    localStorage.setItem('user', JSON.stringify(userData));
    setUser(userData);

    return userData;
  };

  const handleOAuth2Success = (tokenVal, roleVal, userIdVal, nameVal) => {
    localStorage.setItem('token', tokenVal);
    setToken(tokenVal);

    const userData = { id: Number(userIdVal), role: roleVal, name: nameVal };
    localStorage.setItem('user', JSON.stringify(userData));
    setUser(userData);

    fetchNotifications(userIdVal);
    return userData;
  };

  const logout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    setToken(null);
    setUser(null);
    setNotifications([]);
    setUnreadCount(0);
  };

  const markNotificationRead = async (notificationId) => {
    try {
      await api.patch(`/api/notifications/${notificationId}/read`);
      setNotifications(prev => prev.map(n => n.id === notificationId ? { ...n, read: true } : n));
      setUnreadCount(prev => Math.max(0, prev - 1));
    } catch (err) {
      console.error('Failed to mark notification read:', err);
    }
  };

  return (
    <AuthContext.Provider
      value={{
        token,
        user,
        role: user?.role,
        loading,
        notifications,
        unreadCount,
        login,
        register,
        handleOAuth2Success,
        logout,
        fetchUserProfile,
        fetchNotifications,
        markNotificationRead,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);
