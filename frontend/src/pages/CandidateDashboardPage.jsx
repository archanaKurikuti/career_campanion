import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import api from '../api/axiosConfig';
import { User, FileText, Briefcase, Sparkles, Clock, ArrowRight, Award } from 'lucide-react';

const CandidateDashboardPage = () => {
  const { user } = useAuth();
  const [recommendations, setRecommendations] = useState([]);
  const [applicationsCount, setApplicationsCount] = useState(0);
  const [resumeData, setResumeData] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (user && user.id) {
      loadDashboardData();
    }
  }, [user]);

  const loadDashboardData = async () => {
    setLoading(true);
    try {
      // Load candidate applications count
      const appRes = await api.get(`/api/applications/candidate/${user.id}`);
      setApplicationsCount((appRes.data || []).length);

      // Load candidate resume
      try {
        const resRes = await api.get(`/api/resumes/candidate/${user.id}`);
        setResumeData(resRes.data);
      } catch (e) {
        setResumeData(null);
      }

      // Load AI recommendations
      try {
        const aiRes = await api.get(`/api/ai/recommendations/${user.id}`);
        setRecommendations(aiRes.data || []);
      } catch (e) {
        setRecommendations([]);
      }
    } catch (err) {
      console.error('Failed to load dashboard data:', err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={{ maxWidth: '1280px', margin: '0 auto', padding: '32px 24px' }}>
      
      {/* Welcome Banner */}
      <div className="glass-panel" style={{ padding: '32px', marginBottom: '32px', position: 'relative', overflow: 'hidden' }}>
        <div style={{ position: 'relative', zIndex: 2 }}>
          <div style={{ display: 'flex', alignItems: 'center', gap: '12px', marginBottom: '12px' }}>
            <span className="badge badge-candidate">Candidate Hub</span>
            <span style={{ fontSize: '0.85rem', color: 'var(--text-secondary)' }}>Welcome back</span>
          </div>
          <h1 style={{ fontSize: '2.2rem', marginBottom: '8px' }}>
            Hello, <span className="gradient-text">{user?.name || 'Developer'}</span>!
          </h1>
          <p style={{ color: 'var(--text-secondary)', maxWidth: '640px' }}>
            Track your job applications, inspect AI resume quality audits, and discover top-matched software engineering roles.
          </p>
        </div>
      </div>

      {/* Stats Cards */}
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(240px, 1fr))', gap: '20px', marginBottom: '40px' }}>
        
        <div className="glass-panel" style={{ padding: '24px' }}>
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '12px' }}>
            <span style={{ fontSize: '0.85rem', color: 'var(--text-secondary)' }}>Active Applications</span>
            <Clock size={20} color="var(--accent-blue)" />
          </div>
          <h2 style={{ fontSize: '2.2rem', color: 'var(--accent-blue)' }}>{applicationsCount}</h2>
          <Link to="/applications" style={{ fontSize: '0.8rem', color: 'var(--accent-cyan)', display: 'inline-flex', alignItems: 'center', gap: '4px', marginTop: '8px', fontWeight: 600 }}>
            View Submissions <ArrowRight size={14} />
          </Link>
        </div>

        <div className="glass-panel" style={{ padding: '24px' }}>
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '12px' }}>
            <span style={{ fontSize: '0.85rem', color: 'var(--text-secondary)' }}>Resume Status</span>
            <FileText size={20} color="var(--accent-cyan)" />
          </div>
          <h2 style={{ fontSize: '1.4rem', color: resumeData ? '#34d399' : '#f87171', margin: '6px 0' }}>
            {resumeData ? 'Uploaded & Audited' : 'No Resume'}
          </h2>
          <Link to="/resumes" style={{ fontSize: '0.8rem', color: 'var(--accent-cyan)', display: 'inline-flex', alignItems: 'center', gap: '4px', marginTop: '8px', fontWeight: 600 }}>
            {resumeData ? 'View AI Score' : 'Upload Resume'} <ArrowRight size={14} />
          </Link>
        </div>

        <div className="glass-panel" style={{ padding: '24px' }}>
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '12px' }}>
            <span style={{ fontSize: '0.85rem', color: 'var(--text-secondary)' }}>AI Career Assistant</span>
            <Sparkles size={20} color="var(--accent-purple)" />
          </div>
          <h2 style={{ fontSize: '1.4rem', color: 'var(--accent-purple)', margin: '6px 0' }}>Ready to Help</h2>
          <Link to="/ai-assistant" style={{ fontSize: '0.8rem', color: 'var(--accent-purple)', display: 'inline-flex', alignItems: 'center', gap: '4px', marginTop: '8px', fontWeight: 600 }}>
            Ask Question <ArrowRight size={14} />
          </Link>
        </div>

      </div>

      {/* AI Job Recommendations Section */}
      <div style={{ marginBottom: '40px' }}>
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '20px' }}>
          <div>
            <h2 style={{ fontSize: '1.5rem', display: 'flex', alignItems: 'center', gap: '8px' }}>
              <Sparkles size={20} color="var(--accent-cyan)" /> AI Recommended Jobs
            </h2>
            <p style={{ fontSize: '0.85rem', color: 'var(--text-secondary)' }}>
              Top roles matched automatically based on your resume and skill matrix.
            </p>
          </div>
          <Link to="/jobs" className="btn-secondary" style={{ fontSize: '0.85rem' }}>
            Browse All Jobs
          </Link>
        </div>

        {recommendations.length === 0 ? (
          <div className="glass-panel" style={{ padding: '36px', textAlign: 'center', color: 'var(--text-secondary)' }}>
            No recommendations generated yet. Upload your resume to get automated AI recommendations!
          </div>
        ) : (
          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(320px, 1fr))', gap: '20px' }}>
            {recommendations.slice(0, 3).map(rec => (
              <div key={rec.jobId} className="glass-panel glass-panel-hover" style={{ padding: '24px' }}>
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', marginBottom: '12px' }}>
                  <h3 style={{ fontSize: '1.1rem' }}>{rec.jobTitle}</h3>
                  <span className="badge badge-candidate" style={{ fontSize: '0.75rem', fontWeight: 700 }}>
                    {rec.matchScore}% Match
                  </span>
                </div>
                <p style={{ fontSize: '0.85rem', color: 'var(--text-secondary)', marginBottom: '16px' }}>
                  {rec.matchAnalysis}
                </p>
                <Link to="/jobs" className="btn-primary" style={{ width: '100%', justifyContent: 'center', fontSize: '0.8rem', padding: '8px' }}>
                  View & Apply
                </Link>
              </div>
            ))}
          </div>
        )}
      </div>

    </div>
  );
};

export default CandidateDashboardPage;
