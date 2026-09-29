import React, { useState, useEffect } from 'react';
import { useAuth } from '../context/AuthContext';
import api from '../api/axiosConfig';
import { Clock, Building2, MapPin, CheckCircle2, FileText, AlertCircle } from 'lucide-react';

const MyApplicationsPage = () => {
  const { user } = useAuth();
  const [applications, setApplications] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (user && user.id) {
      fetchApplications();
    }
  }, [user]);

  const fetchApplications = async () => {
    setLoading(true);
    try {
      const res = await api.get(`/api/applications/candidate/${user.id}`);
      setApplications(res.data || []);
    } catch (err) {
      console.error('Failed to fetch applications:', err);
    } finally {
      setLoading(false);
    }
  };

  const getStatusBadgeClass = (status) => {
    switch (status) {
      case 'SHORTLISTED': return 'badge badge-status-shortlisted';
      case 'INTERVIEWED': return 'badge badge-status-interviewed';
      case 'OFFERED': return 'badge badge-status-offered';
      case 'REJECTED': return 'badge badge-status-rejected';
      default: return 'badge badge-status-applied';
    }
  };

  return (
    <div style={{ maxWidth: '1000px', margin: '0 auto', padding: '32px 24px' }}>
      
      <div style={{ textAlign: 'center', marginBottom: '36px' }}>
        <h1 style={{ fontSize: '2.4rem', marginBottom: '8px' }}>
          My Job <span className="gradient-text">Applications</span>
        </h1>
        <p style={{ color: 'var(--text-secondary)' }}>
          Track the real-time status of your submitted job applications.
        </p>
      </div>

      {loading ? (
        <div style={{ textAlign: 'center', padding: '60px 0', color: 'var(--text-secondary)' }}>
          Loading application history...
        </div>
      ) : applications.length === 0 ? (
        <div className="glass-panel" style={{ padding: '60px', textAlign: 'center' }}>
          <Clock size={40} color="var(--text-muted)" style={{ marginBottom: '16px' }} />
          <h3>No applications submitted yet</h3>
          <p style={{ color: 'var(--text-secondary)', fontSize: '0.9rem', marginTop: '6px' }}>
            Head over to the Job Board and apply to software engineering positions!
          </p>
        </div>
      ) : (
        <div style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
          {applications.map(app => (
            <div key={app.id} className="glass-panel glass-panel-hover" style={{ padding: '24px' }}>
              
              <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', flexWrap: 'wrap', gap: '12px', marginBottom: '12px' }}>
                <div>
                  <h3 style={{ fontSize: '1.2rem', marginBottom: '4px' }}>{app.jobTitle || 'Software Position'}</h3>
                  <p style={{ fontSize: '0.85rem', color: 'var(--text-secondary)', display: 'flex', alignItems: 'center', gap: '6px' }}>
                    <Building2 size={14} color="var(--accent-cyan)" /> {app.companyName || 'Employer'}
                  </p>
                </div>
                <span className={getStatusBadgeClass(app.status)}>
                  {app.status}
                </span>
              </div>

              <div style={{ background: 'rgba(15, 23, 42, 0.4)', borderRadius: '8px', padding: '14px', marginBottom: '16px' }}>
                <p style={{ fontSize: '0.8rem', fontWeight: 600, color: 'var(--text-muted)', marginBottom: '4px' }}>
                  Cover Letter Submission:
                </p>
                <p style={{ fontSize: '0.85rem', color: 'var(--text-secondary)' }}>
                  "{app.coverLetter || 'No cover letter submitted.'}"
                </p>
              </div>

              {app.recruiterNotes && (
                <div style={{ background: 'rgba(139, 92, 246, 0.1)', border: '1px solid rgba(139, 92, 246, 0.3)', borderRadius: '8px', padding: '12px' }}>
                  <p style={{ fontSize: '0.85rem', color: 'var(--accent-purple)', fontWeight: 600 }}>
                    Recruiter Feedback:
                  </p>
                  <p style={{ fontSize: '0.85rem', color: 'var(--text-primary)' }}>
                    {app.recruiterNotes}
                  </p>
                </div>
              )}

              <div style={{ marginTop: '12px', textAlign: 'right', fontSize: '0.75rem', color: 'var(--text-muted)' }}>
                Submitted: {app.appliedAt ? new Date(app.appliedAt).toLocaleDateString() : 'Recently'}
              </div>

            </div>
          ))}
        </div>
      )}

    </div>
  );
};

export default MyApplicationsPage;
