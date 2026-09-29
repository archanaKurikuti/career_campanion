import React, { useState, useEffect } from 'react';
import { useAuth } from '../context/AuthContext';
import api from '../api/axiosConfig';
import { UploadCloud, FileText, Sparkles, CheckCircle, AlertTriangle, Lightbulb, Award, Trash2 } from 'lucide-react';

const ResumeUploadPage = () => {
  const { user } = useAuth();
  const [file, setFile] = useState(null);
  const [loading, setLoading] = useState(false);
  const [resumeData, setResumeData] = useState(null);
  const [error, setError] = useState('');

  useEffect(() => {
    if (user && user.id) {
      fetchExistingResume();
    }
  }, [user]);

  const fetchExistingResume = async () => {
    try {
      const res = await api.get(`/api/resumes/candidate/${user.id}`);
      setResumeData(res.data);
    } catch (err) {
      console.log('No existing resume found for candidate');
    }
  };

  const handleFileChange = (e) => {
    if (e.target.files && e.target.files[0]) {
      setFile(e.target.files[0]);
    }
  };

  const handleUploadSubmit = async (e) => {
    e.preventDefault();
    if (!file || !user) return;
    setLoading(true);
    setError('');

    const formData = new FormData();
    formData.append('file', file);

    try {
      const res = await api.post(`/api/resumes/upload/${user.id}`, formData, {
        headers: { 'Content-Type': 'multipart/form-data' },
      });
      setResumeData(res.data);
      setFile(null);
    } catch (err) {
      console.error('Failed to upload resume:', err);
      setError(err.response?.data?.message || 'Failed to upload resume file.');
    } finally {
      setLoading(false);
    }
  };

  const handleDeleteResume = async () => {
    if (!resumeData || !resumeData.id) return;
    try {
      await api.delete(`/api/resumes/${resumeData.id}`);
      setResumeData(null);
    } catch (err) {
      console.error('Failed to delete resume:', err);
    }
  };

  const aiAnalysis = resumeData?.aiAnalysis;

  return (
    <div style={{ maxWidth: '1000px', margin: '0 auto', padding: '32px 24px' }}>
      
      <div style={{ textAlign: 'center', marginBottom: '36px' }}>
        <h1 style={{ fontSize: '2.4rem', marginBottom: '8px' }}>
          AI Resume <span className="gradient-text">Audit & Scanner</span>
        </h1>
        <p style={{ color: 'var(--text-secondary)' }}>
          Upload your resume to receive instant quality scoring, skill extraction, and AI recommendations.
        </p>
      </div>

      {error && (
        <div style={{ background: 'rgba(239, 68, 68, 0.15)', color: '#f87171', padding: '12px', borderRadius: '8px', marginBottom: '24px', fontSize: '0.85rem' }}>
          {error}
        </div>
      )}

      {/* Upload Zone */}
      <div className="glass-panel" style={{ padding: '36px', textAlign: 'center', marginBottom: '40px' }}>
        <form onSubmit={handleUploadSubmit}>
          <div style={{ 
            border: '2px dashed var(--border-glow)', 
            borderRadius: '16px', 
            padding: '40px 20px', 
            background: 'rgba(15, 23, 42, 0.4)', 
            marginBottom: '20px',
            cursor: 'pointer'
          }}>
            <UploadCloud size={48} color="var(--accent-cyan)" style={{ marginBottom: '12px' }} />
            <h3 style={{ fontSize: '1.2rem', marginBottom: '6px' }}>
              {file ? file.name : 'Select or Drag & Drop Resume File'}
            </h3>
            <p style={{ fontSize: '0.85rem', color: 'var(--text-secondary)', marginBottom: '16px' }}>
              Supports PDF, DOCX, and TXT files up to 10MB
            </p>
            <input 
              type="file" 
              accept=".pdf,.docx,.txt" 
              onChange={handleFileChange}
              style={{ display: 'none' }}
              id="resume-file-input"
            />
            <label htmlFor="resume-file-input" className="btn-secondary" style={{ cursor: 'pointer' }}>
              Browse Files
            </label>
          </div>

          <button 
            type="submit" 
            className="btn-primary" 
            disabled={!file || loading}
            style={{ padding: '12px 32px', fontSize: '0.95rem' }}
          >
            {loading ? 'Uploading & Analyzing with AI...' : <>Upload & Run AI Audit <Sparkles size={16} /></>}
          </button>
        </form>
      </div>

      {/* AI Resume Analysis Report */}
      {resumeData && (
        <div className="glass-panel animate-fade-in" style={{ padding: '36px' }}>
          
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '24px', paddingBottom: '16px', borderBottom: '1px solid var(--border-subtle)' }}>
            <div>
              <span className="badge badge-candidate" style={{ marginBottom: '6px', display: 'inline-block' }}>
                Audited Document
              </span>
              <h2 style={{ fontSize: '1.5rem' }}>{resumeData.fileName}</h2>
            </div>
            <button onClick={handleDeleteResume} className="btn-danger" style={{ display: 'flex', alignItems: 'center', gap: '6px' }}>
              <Trash2 size={16} /> Delete Resume
            </button>
          </div>

          {aiAnalysis ? (
            <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(280px, 1fr))', gap: '24px' }}>
              
              {/* Quality Score Meter */}
              <div className="glass-panel" style={{ padding: '24px', textAlign: 'center', background: 'rgba(6, 182, 212, 0.05)' }}>
                <span style={{ fontSize: '0.8rem', fontWeight: 700, color: 'var(--accent-cyan)', textTransform: 'uppercase' }}>
                  Resume Quality Score
                </span>
                <div style={{ fontSize: '3.5rem', fontWeight: 800, margin: '12px 0' }} className="gradient-text">
                  {aiAnalysis.resumeQualityScore}/100
                </div>
                <p style={{ fontSize: '0.85rem', color: 'var(--text-secondary)' }}>
                  {aiAnalysis.experienceSummary}
                </p>
              </div>

              {/* Detected Skills */}
              <div className="glass-panel" style={{ padding: '24px' }}>
                <h4 style={{ fontSize: '1rem', marginBottom: '12px', display: 'flex', alignItems: 'center', gap: '8px' }}>
                  <Award size={18} color="var(--accent-purple)" /> Extracted Skill Matrix
                </h4>
                <div style={{ display: 'flex', flexWrap: 'wrap', gap: '8px' }}>
                  {aiAnalysis.detectedSkills?.map((skill, idx) => (
                    <span key={idx} className="badge badge-recruiter" style={{ fontSize: '0.75rem' }}>
                      {skill}
                    </span>
                  ))}
                </div>
              </div>

              {/* Strengths */}
              <div className="glass-panel" style={{ padding: '24px', gridColumn: 'span 1' }}>
                <h4 style={{ fontSize: '1rem', marginBottom: '12px', color: '#34d399', display: 'flex', alignItems: 'center', gap: '8px' }}>
                  <CheckCircle size={18} /> Strengths
                </h4>
                <ul style={{ listStyle: 'none', fontSize: '0.85rem', color: 'var(--text-secondary)' }}>
                  {aiAnalysis.strengths?.map((str, idx) => (
                    <li key={idx} style={{ marginBottom: '8px', paddingLeft: '16px', position: 'relative' }}>
                      <span style={{ position: 'absolute', left: 0, color: '#34d399' }}>✓</span> {str}
                    </li>
                  ))}
                </ul>
              </div>

              {/* Suggested Improvements */}
              <div className="glass-panel" style={{ padding: '24px', gridColumn: 'span 1' }}>
                <h4 style={{ fontSize: '1rem', marginBottom: '12px', color: 'var(--accent-amber)', display: 'flex', alignItems: 'center', gap: '8px' }}>
                  <Lightbulb size={18} /> Suggested Improvements
                </h4>
                <ul style={{ listStyle: 'none', fontSize: '0.85rem', color: 'var(--text-secondary)' }}>
                  {aiAnalysis.suggestedImprovements?.map((imp, idx) => (
                    <li key={idx} style={{ marginBottom: '8px', paddingLeft: '16px', position: 'relative' }}>
                      <span style={{ position: 'absolute', left: 0, color: 'var(--accent-amber)' }}>•</span> {imp}
                    </li>
                  ))}
                </ul>
              </div>

            </div>
          ) : (
            <p style={{ color: 'var(--text-secondary)' }}>
              Resume file uploaded successfully. Summary: {resumeData.summary}
            </p>
          )}

        </div>
      )}

    </div>
  );
};

export default ResumeUploadPage;
