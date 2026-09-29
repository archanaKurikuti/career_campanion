import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import api from '../api/axiosConfig';
import { Building2, Briefcase, UserCheck, Plus, ArrowRight, Layers, Users } from 'lucide-react';

const RecruiterDashboardPage = () => {
  const { user } = useAuth();
  const [recruiterProfile, setRecruiterProfile] = useState(null);
  const [jobs, setJobs] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (user && user.id) {
      loadRecruiterData();
    }
  }, [user]);

  const loadRecruiterData = async () => {
    setLoading(true);
    try {
      const res = await api.get(`/api/recruiters/${user.id}`);
      setRecruiterProfile(res.data);

      const jobsRes = await api.get('/api/jobs');
      const companyJobs = (jobsRes.data.content || []).filter(j => j.recruiterId === user.id || (res.data.companyId && j.companyId === res.data.companyId));
      setJobs(companyJobs);
    } catch (err) {
      console.error('Failed to load recruiter dashboard:', err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={{ maxWidth: '1280px', margin: '0 auto', padding: '32px 24px' }}>
      
      {/* Welcome Banner */}
      <div className="glass-panel" style={{ padding: '32px', marginBottom: '32px' }}>
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', flexWrap: 'wrap', gap: '16px' }}>
          <div>
            <div style={{ display: 'flex', alignItems: 'center', gap: '12px', marginBottom: '8px' }}>
              <span className="badge badge-recruiter">Recruiter Workspace</span>
              <span style={{ fontSize: '0.85rem', color: 'var(--text-secondary)' }}>
                {recruiterProfile?.companyName || 'Manage Hiring'}
              </span>
            </div>
            <h1 style={{ fontSize: '2.2rem', marginBottom: '4px' }}>
              Recruiter Dashboard
            </h1>
            <p style={{ color: 'var(--text-secondary)' }}>
              Post technical positions, manage company details, and evaluate candidate applications.
            </p>
          </div>

          <div style={{ display: 'flex', gap: '12px' }}>
            <Link to="/recruiter/post-job" className="btn-primary">
              <Plus size={16} /> Post New Job
            </Link>
            <Link to="/recruiter/company" className="btn-secondary">
              <Building2 size={16} /> Edit Company
            </Link>
          </div>
        </div>
      </div>

      {/* Metrics Row */}
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(240px, 1fr))', gap: '20px', marginBottom: '40px' }}>
        
        <div className="glass-panel" style={{ padding: '24px' }}>
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '12px' }}>
            <span style={{ fontSize: '0.85rem', color: 'var(--text-secondary)' }}>Posted Openings</span>
            <Briefcase size={20} color="var(--accent-purple)" />
          </div>
          <h2 style={{ fontSize: '2.2rem', color: 'var(--accent-purple)' }}>{jobs.length}</h2>
          <span style={{ fontSize: '0.8rem', color: 'var(--text-muted)' }}>Active tech job listings</span>
        </div>

        <div className="glass-panel" style={{ padding: '24px' }}>
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '12px' }}>
            <span style={{ fontSize: '0.85rem', color: 'var(--text-secondary)' }}>Company Profile</span>
            <Building2 size={20} color="var(--accent-cyan)" />
          </div>
          <h2 style={{ fontSize: '1.4rem', color: recruiterProfile?.companyId ? '#34d399' : '#f87171', margin: '6px 0' }}>
            {recruiterProfile?.companyName || 'Not Linked'}
          </h2>
          <Link to="/recruiter/company" style={{ fontSize: '0.8rem', color: 'var(--accent-cyan)', fontWeight: 600 }}>
            Manage Profile <ArrowRight size={14} />
          </Link>
        </div>

        <div className="glass-panel" style={{ padding: '24px' }}>
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '12px' }}>
            <span style={{ fontSize: '0.85rem', color: 'var(--text-secondary)' }}>Applicant Review</span>
            <Users size={20} color="var(--accent-emerald)" />
          </div>
          <h2 style={{ fontSize: '1.4rem', color: 'var(--accent-emerald)', margin: '6px 0' }}>Review Submissions</h2>
          <Link to="/recruiter/applicants" style={{ fontSize: '0.8rem', color: 'var(--accent-emerald)', fontWeight: 600 }}>
            Inspect Applicants <ArrowRight size={14} />
          </Link>
        </div>

      </div>

      {/* Posted Jobs Table */}
      <div className="glass-panel" style={{ padding: '24px' }}>
        <h3 style={{ fontSize: '1.25rem', marginBottom: '20px' }}>Your Job Openings</h3>

        {jobs.length === 0 ? (
          <p style={{ color: 'var(--text-secondary)', fontSize: '0.9rem', textAlign: 'center', padding: '24px 0' }}>
            No jobs posted yet. Click "Post New Job" to list software engineering roles.
          </p>
        ) : (
          <div style={{ overflowX: 'auto' }}>
            <table style={{ width: '100%', borderCollapse: 'collapse', textAlign: 'left', fontSize: '0.9rem' }}>
              <thead>
                <tr style={{ borderBottom: '1px solid var(--border-subtle)', color: 'var(--text-secondary)' }}>
                  <th style={{ padding: '12px' }}>Job Title</th>
                  <th style={{ padding: '12px' }}>Location</th>
                  <th style={{ padding: '12px' }}>Salary</th>
                  <th style={{ padding: '12px' }}>Job Type</th>
                  <th style={{ padding: '12px', textAlign: 'right' }}>Actions</th>
                </tr>
              </thead>
              <tbody>
                {jobs.map(j => (
                  <tr key={j.id} style={{ borderBottom: '1px solid rgba(255, 255, 255, 0.05)' }}>
                    <td style={{ padding: '12px', fontWeight: 600 }}>{j.title}</td>
                    <td style={{ padding: '12px', color: 'var(--text-secondary)' }}>{j.location || 'Remote'}</td>
                    <td style={{ padding: '12px', color: 'var(--accent-cyan)' }}>${j.salary?.toLocaleString()}/yr</td>
                    <td style={{ padding: '12px' }}><span className="badge badge-candidate">{j.jobType}</span></td>
                    <td style={{ padding: '12px', textAlign: 'right' }}>
                      <Link to="/recruiter/applicants" className="btn-secondary" style={{ fontSize: '0.75rem', padding: '4px 10px' }}>
                        Inspect Applicants
                      </Link>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>

    </div>
  );
};

export default RecruiterDashboardPage;
