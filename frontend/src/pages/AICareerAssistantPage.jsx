import React, { useState } from 'react';
import { useAuth } from '../context/AuthContext';
import api from '../api/axiosConfig';
import { Sparkles, Send, User, Bot, Lightbulb } from 'lucide-react';

const AICareerAssistantPage = () => {
  const { user } = useAuth();
  const [messages, setMessages] = useState([
    {
      sender: 'ai',
      text: `Hello ${user?.name || 'there'}! I am your AI Career Companion. Ask me anything about resume tuning, interview questions, software career roadmaps, or compensation guidance.`
    }
  ]);
  const [prompt, setPrompt] = useState('');
  const [loading, setLoading] = useState(false);

  const handleSend = async (e) => {
    e.preventDefault();
    if (!prompt.trim()) return;

    if (!user || !localStorage.getItem('token')) {
      setMessages(prev => [...prev, { sender: 'ai', text: 'Please log in to use the AI Career Assistant.' }]);
      window.location.href = '/login';
      return;
    }

    const userMsg = prompt.trim();
    setPrompt('');
    setMessages(prev => [...prev, { sender: 'user', text: userMsg }]);
    setLoading(true);

    try {
      const res = await api.post('/api/ai/career-advice', {
        question: userMsg,
        candidateId: user?.id,
      });

      setMessages(prev => [
        ...prev,
        { sender: 'ai', text: res.data.advice || 'Keep learning and practicing your data structures and system design fundamentals!' }
      ]);
    } catch (err) {
      console.error('AI assistant error:', err);
      setMessages(prev => [
        ...prev,
        { sender: 'ai', text: 'Sorry, I ran into an error generating career advice. Please try asking again!' }
      ]);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={{ maxWidth: '900px', margin: '0 auto', padding: '32px 24px' }}>
      
      <div style={{ textAlign: 'center', marginBottom: '28px' }}>
        <h1 style={{ fontSize: '2.4rem', marginBottom: '8px' }}>
          AI Career <span className="gradient-text">Assistant</span>
        </h1>
        <p style={{ color: 'var(--text-secondary)' }}>
          Instant, personalized career coaching powered by Career Companion AI.
        </p>
      </div>

      {/* Quick Prompt Ideas */}
      <div style={{ display: 'flex', gap: '10px', flexWrap: 'wrap', justifyContent: 'center', marginBottom: '24px' }}>
        <button 
          onClick={() => setPrompt("How do I prepare for a Senior Java Developer interview?")}
          className="btn-secondary" 
          style={{ fontSize: '0.8rem', padding: '6px 12px' }}
        >
          <Lightbulb size={14} color="var(--accent-amber)" /> Interview Prep
        </button>
        <button 
          onClick={() => setPrompt("What skills are most in-demand for Spring Boot & React engineers?")}
          className="btn-secondary" 
          style={{ fontSize: '0.8rem', padding: '6px 12px' }}
        >
          <Lightbulb size={14} color="var(--accent-cyan)" /> Skill Roadmap
        </button>
        <button 
          onClick={() => setPrompt("How can I improve my resume's ATS score?")}
          className="btn-secondary" 
          style={{ fontSize: '0.8rem', padding: '6px 12px' }}
        >
          <Lightbulb size={14} color="var(--accent-purple)" /> Resume Tuning
        </button>
      </div>

      {/* Chat Container */}
      <div className="glass-panel" style={{ padding: '24px', height: '480px', display: 'flex', flexDirection: 'column' }}>
        
        <div style={{ flex: 1, overflowY: 'auto', display: 'flex', flexDirection: 'column', gap: '16px', paddingRight: '8px' }}>
          {messages.map((m, idx) => (
            <div 
              key={idx} 
              style={{ 
                alignSelf: m.sender === 'user' ? 'flex-end' : 'flex-start',
                maxWidth: '80%',
                display: 'flex',
                gap: '10px',
                flexDirection: m.sender === 'user' ? 'row-reverse' : 'row'
              }}
            >
              <div style={{ 
                width: '32px', 
                height: '32px', 
                borderRadius: '50%', 
                background: m.sender === 'user' ? 'var(--accent-blue)' : 'linear-gradient(135deg, #8b5cf6 0%, #06b6d4 100%)', 
                display: 'flex', 
                alignItems: 'center', 
                justifyContent: 'center',
                flexShrink: 0
              }}>
                {m.sender === 'user' ? <User size={16} color="white" /> : <Bot size={16} color="white" />}
              </div>

              <div style={{ 
                background: m.sender === 'user' ? 'rgba(59, 130, 246, 0.2)' : 'rgba(255, 255, 255, 0.06)', 
                border: '1px solid var(--border-subtle)', 
                padding: '12px 16px', 
                borderRadius: '16px', 
                fontSize: '0.9rem', 
                lineHeight: 1.6,
                color: 'var(--text-primary)'
              }}>
                {m.text}
              </div>
            </div>
          ))}
          {loading && (
            <div style={{ alignSelf: 'flex-start', display: 'flex', gap: '8px', color: 'var(--text-secondary)', fontSize: '0.85rem' }}>
              <Sparkles size={16} className="pulse-glow" color="var(--accent-purple)" /> Thinking...
            </div>
          )}
        </div>

        {/* Input Bar */}
        <form onSubmit={handleSend} style={{ display: 'flex', gap: '12px', marginTop: '16px', paddingTop: '16px', borderTop: '1px solid var(--border-subtle)' }}>
          <input 
            type="text" 
            className="input-field" 
            placeholder="Ask your career question..."
            value={prompt}
            onChange={(e) => setPrompt(e.target.value)}
          />
          <button type="submit" className="btn-primary" disabled={loading || !prompt.trim()} style={{ padding: '0 20px' }}>
            <Send size={18} />
          </button>
        </form>

      </div>

    </div>
  );
};

export default AICareerAssistantPage;
