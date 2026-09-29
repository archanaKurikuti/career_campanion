import React from 'react';
import { Briefcase } from 'lucide-react';

const Footer = () => {
  return (
    <footer style={{
      borderTop: '1px solid var(--border)',
      background: 'var(--bg-primary)',
      padding: '32px 24px 20px',
      marginTop: '40px',
    }}>
      <div style={{ maxWidth: '1200px', margin: '0 auto', display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(220px, 1fr))', gap: '24px', marginBottom: '24px' }}>

        <div>
          <div style={{ display: 'flex', alignItems: 'center', gap: '8px', marginBottom: '10px' }}>
            <Briefcase size={18} color="var(--accent)" />
            <span style={{ fontSize: '1rem', fontWeight: 700 }}>Career Companion</span>
          </div>
          <p style={{ fontSize: '0.8rem', color: 'var(--text-secondary)', lineHeight: 1.6 }}>
            AI-Powered Job & Career Management Platform. Smart matching, resume auditing, and career guidance.
          </p>
        </div>

        <div>
          <h4 style={{ fontSize: '0.85rem', marginBottom: '12px', color: 'var(--text-primary)' }}>Tech Stack</h4>
          <ul style={{ listStyle: 'none', fontSize: '0.8rem', color: 'var(--text-secondary)', lineHeight: 2 }}>
            <li>Java 21 & Spring Boot</li>
            <li>Spring Security & JWT</li>
            <li>OAuth2 (Google & GitHub)</li>
            <li>MySQL & JPA</li>
            <li>React & Vite</li>
          </ul>
        </div>

        <div>
          <h4 style={{ fontSize: '0.85rem', marginBottom: '12px', color: 'var(--text-primary)' }}>AI Features</h4>
          <ul style={{ listStyle: 'none', fontSize: '0.8rem', color: 'var(--text-secondary)', lineHeight: 2 }}>
            <li>Resume Quality Scoring</li>
            <li>Skill Extraction</li>
            <li>Job Match Scoring</li>
            <li>Career Guidance</li>
          </ul>
        </div>
      </div>

      <div style={{ maxWidth: '1200px', margin: '0 auto', paddingTop: '16px', borderTop: '1px solid var(--border)', display: 'flex', justifyContent: 'space-between', alignItems: 'center', flexWrap: 'wrap', gap: '8px', fontSize: '0.75rem', color: 'var(--text-muted)' }}>
        <p>© 2026 Career Companion. All rights reserved.</p>
        <p>Built for modern job seekers and recruiters.</p>
      </div>
    </footer>
  );
};

export default Footer;
