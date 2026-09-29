import React from 'react';
import { Link } from 'react-router-dom';
import { Briefcase, Sparkles, Target, FileText, CheckCircle, ArrowRight, Award, Zap } from 'lucide-react';

const LandingPage = () => {
  return (
    <div style={{ maxWidth: '1280px', margin: '0 auto', padding: '40px 24px' }}>
      
      {/* Hero Section */}
      <section style={{ textAlign: 'center', padding: '60px 0 80px', position: 'relative' }}>
        <div style={{ 
          display: 'inline-flex', 
          alignItems: 'center', 
          gap: '8px', 
          background: 'rgba(6, 182, 212, 0.1)', 
          border: '1px solid rgba(6, 182, 212, 0.3)', 
          padding: '6px 16px', 
          borderRadius: '30px', 
          fontSize: '0.85rem', 
          fontWeight: 600, 
          color: 'var(--accent-cyan)',
          marginBottom: '24px' 
        }}>
          <Sparkles size={16} /> Next-Gen AI Career Companion Platform
        </div>

        <h1 style={{ fontSize: '3.5rem', lineHeight: 1.1, marginBottom: '24px' }}>
          Supercharge Your Career with <br />
          <span className="gradient-text">AI-Powered Job Matching</span>
        </h1>

        <p style={{ fontSize: '1.2rem', color: 'var(--text-secondary)', maxWidth: '720px', margin: '0 auto 36px' }}>
          Instant AI resume auditing, automated skill extraction, intelligent job match scoring, and real-time recruitment workflows built for Candidates & Recruiters.
        </p>

        <div style={{ display: 'flex', justifyContent: 'center', gap: '16px', flexWrap: 'wrap' }}>
          <Link to="/register" className="btn-primary" style={{ fontSize: '1rem', padding: '14px 28px' }}>
            Get Started Free <ArrowRight size={18} />
          </Link>
          <Link to="/jobs" className="btn-secondary" style={{ fontSize: '1rem', padding: '14px 28px' }}>
            Browse Open Jobs
          </Link>
        </div>

        {/* Floating Quick Stats */}
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(200px, 1fr))', gap: '20px', marginTop: '60px' }}>
          <div className="glass-panel" style={{ padding: '20px', textAlign: 'center' }}>
            <h3 style={{ fontSize: '2rem', color: 'var(--accent-cyan)' }}>98%</h3>
            <p style={{ fontSize: '0.85rem', color: 'var(--text-secondary)' }}>AI Resume Parsing Accuracy</p>
          </div>
          <div className="glass-panel" style={{ padding: '20px', textAlign: 'center' }}>
            <h3 style={{ fontSize: '2rem', color: 'var(--accent-purple)' }}>500+</h3>
            <p style={{ fontSize: '0.85rem', color: 'var(--text-secondary)' }}>Verified Job Openings</p>
          </div>
          <div className="glass-panel" style={{ padding: '20px', textAlign: 'center' }}>
            <h3 style={{ fontSize: '2rem', color: 'var(--accent-emerald)' }}>10x</h3>
            <p style={{ fontSize: '0.85rem', color: 'var(--text-secondary)' }}>Faster Hiring Workflows</p>
          </div>
          <div className="glass-panel" style={{ padding: '20px', textAlign: 'center' }}>
            <h3 style={{ fontSize: '2rem', color: 'var(--accent-amber)' }}>Instant</h3>
            <p style={{ fontSize: '0.85rem', color: 'var(--text-secondary)' }}>OAuth2 SSO & JWT Auth</p>
          </div>
        </div>
      </section>

      {/* Feature Cards Grid */}
      <section style={{ padding: '60px 0' }}>
        <div style={{ textAlign: 'center', marginBottom: '48px' }}>
          <h2 style={{ fontSize: '2.2rem', marginBottom: '12px' }}>Engineered for Excellence</h2>
          <p style={{ color: 'var(--text-secondary)' }}>Everything you need to land your dream job or recruit top-tier talent.</p>
        </div>

        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(320px, 1fr))', gap: '24px' }}>
          
          <div className="glass-panel glass-panel-hover" style={{ padding: '32px' }}>
            <div style={{ width: '48px', height: '48px', borderRadius: '12px', background: 'rgba(6, 182, 212, 0.15)', display: 'flex', alignItems: 'center', justifyContent: 'center', color: 'var(--accent-cyan)', marginBottom: '20px' }}>
              <FileText size={24} />
            </div>
            <h3 style={{ fontSize: '1.25rem', marginBottom: '10px' }}>AI Resume Auditor</h3>
            <p style={{ fontSize: '0.9rem', color: 'var(--text-secondary)', lineHeight: 1.6 }}>
              Upload your PDF/DOCX resume and receive an instant quality score (0-100), detected skills list, strengths, weaknesses, and actionable improvement tips.
            </p>
          </div>

          <div className="glass-panel glass-panel-hover" style={{ padding: '32px' }}>
            <div style={{ width: '48px', height: '48px', borderRadius: '12px', background: 'rgba(139, 92, 246, 0.15)', display: 'flex', alignItems: 'center', justifyContent: 'center', color: 'var(--accent-purple)', marginBottom: '20px' }}>
              <Target size={24} />
            </div>
            <h3 style={{ fontSize: '1.25rem', marginBottom: '10px' }}>Intelligent Job Matching</h3>
            <p style={{ fontSize: '0.9rem', color: 'var(--text-secondary)', lineHeight: 1.6 }}>
              Calculate instant match percentage scores between candidate profiles and job requirements. Discover personalized recommendations tailored to your skillset.
            </p>
          </div>

          <div className="glass-panel glass-panel-hover" style={{ padding: '32px' }}>
            <div style={{ width: '48px', height: '48px', borderRadius: '12px', background: 'rgba(16, 185, 129, 0.15)', display: 'flex', alignItems: 'center', justifyContent: 'center', color: 'var(--accent-emerald)', marginBottom: '20px' }}>
              <Zap size={24} />
            </div>
            <h3 style={{ fontSize: '1.25rem', marginBottom: '10px' }}>AI Career Assistant</h3>
            <p style={{ fontSize: '0.9rem', color: 'var(--text-secondary)', lineHeight: 1.6 }}>
              Get answers to career questions, interview preparation strategies, negotiation tips, and salary benchmarks powered by your intelligent companion.
            </p>
          </div>

        </div>
      </section>

    </div>
  );
};

export default LandingPage;
