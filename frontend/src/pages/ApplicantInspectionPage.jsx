import React, { useState, useEffect } from 'react';
import { useAuth } from '../context/AuthContext';
import api from '../api/axiosConfig';
import { Users, FileText, CheckCircle, XCircle, Clock, Send, MessageSquare } from 'lucide-react';

const ApplicantInspectionPage = () => {
  const { user } = useAuth();
  const [applications, setApplications] = useState([]);
  const [loading, setLoading] = useState(true);
  const [selectedApp, setSelectedApp] = useState(null);
  const [status, setStatus] = useState('SHORTLISTED');
  const [notes, setNotes] = useState('');
  const [updating, setUpdating] = useState(false);

  useEffect(() => {
    if (user && user.id) {
      fetchRecruiterApplications();
    }
  }, [user]);

  const fetchRecruiterApplications = async () => {
    setLoading(true);
    try {
      const res = await api.get(`/api/applications/recruiter/${user.id}`);
      setApplications(res.data || []);
    } catch (err) {
      console.error('Failed to fetch recruiter applications:', err);
    } finally {
      setLoading(false);
    }
  };

  const handleUpdateStatus = async (e) => {
    e.preventDefault();
    if (!selectedApp) return;
    setUpdating(true);

    try {
      await api.put(`/api/applications/${selectedApp.id}/status`, null, {
        params: {
          status,
          notes: notes || undefined
        }
      });
      fetchRecruiterApplications();
      setSelectedApp(null);
      setNotes('');
    } catch (err) {
      console.error('Failed to update status:', err);
    } finally {
      setUpdating(false);
    }
  };

  const getBadgeStyle = (st) => {
    switch (st) {
      case 'SHORTLISTED': return 'badge badge-status-shortlisted';
      case 'INTERVIEWED': return 'badge badge-status-interviewed';
      case 'OFFERED': return 'badge badge-status-offered';
      case 'REJECTED': return 'badge badge-status-rejected';
      default: return 'badge badge-status-applied';
    }
  };

  return (
    <div style={{ maxWidth: '1100px', margin: '0 auto', padding: '32px 24px' }}>
      
      <div style={{ textAlign: 'center', marginBottom: '32px' }}>
        <h1 style={{ fontSize: '2.4rem', marginBottom: '8px' }}>
          Applicant <span className="gradient-text">Inspection</span>
        </h1>
        <p style={{ color: 'var(--text-secondary)' }}>
          Review candidate profiles, inspect cover letters, and update hiring pipeline status.
        </p>
      </div>

      {loading ? (
        <div style={{ textAlign: 'center', padding: '60px 0', color: 'var(--text-secondary)' }}>
          Loading applicant submissions...
        </div>
      ) : applications.length === 0 ? (
        <div className="glass-panel" style={{ padding: '60px', textAlign: 'center' }}>
          <Users size={40} color="var(--text-muted)" style={{ marginBottom: '16px' }} />
          <h3>No applications received yet</h3>
          <p style={{ color: 'var(--text-secondary)', fontSize: '0.9rem', marginTop: '6px' }}>
            Check back once candidates submit applications to your posted engineering roles.
          </p>
        </div>
      ) : (
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(320px, 1fr))', gap: '20px' }}>
          {applications.map(app => (
            <div key={app.id} className="glass-panel glass-panel-hover" style={{ padding: '24px', display: 'flex', flexDirection: 'column', justifyContent: 'space-between' }}>
              <div>
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', marginBottom: '12px' }}>
                  <div>
                    <h3 style={{ fontSize: '1.15rem' }}>{app.candidateName || 'Candidate'}</h3>
                    <p style={{ fontSize: '0.8rem', color: 'var(--accent-cyan)' }}>{app.candidateEmail}</p>
                  </div>
                  <span className={getBadgeStyle(app.status)}>
                    {app.status}
                  </span>
                </div>

                <div style={{ background: 'rgba(15, 23, 42, 0.4)', borderRadius: '8px', padding: '12px', marginBottom: '16px' }}>
                  <p style={{ fontSize: '0.75rem', color: 'var(--text-muted)', fontWeight: 600, marginBottom: '4px' }}>
                    Applied Position: {app.jobTitle}
                  </p>
                  <p style={{ fontSize: '0.85rem', color: 'var(--text-secondary)', fontStyle: 'italic' }}>
                    "{app.coverLetter || 'No cover letter provided.'}"
                  </p>
                </div>

                {app.recruiterNotes && (
                  <p style={{ fontSize: '0.8rem', color: 'var(--accent-purple)', marginBottom: '12px' }}>
                    Feedback: {app.recruiterNotes}
                  </p>
                )}
              </div>

              <button 
                onClick={() => {
                  setSelectedApp(app);
                  setStatus(app.status || 'SHORTLISTED');
                  setNotes(app.recruiterNotes || '');
                }}
                className="btn-primary" 
                style={{ width: '100%', justifyContent: 'center', fontSize: '0.85rem', padding: '8px' }}
              >
                Update Pipeline Status
              </button>
            </div>
          ))}
        </div>
      )}

      {/* Update Status Modal */}
      {selectedApp && (
        <div style={{ 
          position: 'fixed', 
          inset: 0, 
          background: 'rgba(0, 0, 0, 0.75)', 
          backdropFilter: 'blur(8px)', 
          zIndex: 300, 
          display: 'flex', 
          alignItems: 'center', 
          justifyContent: 'center', 
          padding: '24px' 
        }}>
          <div className="glass-panel animate-fade-in" style={{ maxWidth: '480px', width: '100%', padding: '32px' }}>
            
            <h3 style={{ fontSize: '1.3rem', marginBottom: '6px' }}>Update Status for {selectedApp.candidateName}</h3>
            <p style={{ fontSize: '0.85rem', color: 'var(--text-secondary)', marginBottom: '20px' }}>
              Position: {selectedApp.jobTitle}
            </p>

            <form onSubmit={handleUpdateStatus} style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
              <div>
                <label style={{ fontSize: '0.8rem', fontWeight: 600, color: 'var(--text-secondary)', display: 'block', marginBottom: '6px' }}>
                  Application Status
                </label>
                <select className="input-field" value={status} onChange={(e) => setStatus(e.target.value)}>
                  <option value="APPLIED">APPLIED</option>
                  <option value="SHORTLISTED">SHORTLISTED</option>
                  <option value="INTERVIEWED">INTERVIEWED</option>
                  <option value="OFFERED">OFFERED</option>
                  <option value="REJECTED">REJECTED</option>
                </select>
              </div>

              <div>
                <label style={{ fontSize: '0.8rem', fontWeight: 600, color: 'var(--text-secondary)', display: 'block', marginBottom: '6px' }}>
                  Recruiter Feedback / Notes
                </label>
                <textarea 
                  className="input-field" 
                  rows={3} 
                  placeholder="Provide interview details, feedback, or instructions..."
                  value={notes}
                  onChange={(e) => setNotes(e.target.value)}
                />
              </div>

              <div style={{ display: 'flex', gap: '10px' }}>
                <button 
                  type="button" 
                  onClick={() => setSelectedApp(null)} 
                  className="btn-secondary" 
                  style={{ flex: 1, justifyContent: 'center' }}
                >
                  Cancel
                </button>
                <button 
                  type="submit" 
                  className="btn-primary" 
                  disabled={updating}
                  style={{ flex: 1, justifyContent: 'center' }}
                >
                  {updating ? 'Saving...' : 'Save & Notify'}
                </button>
              </div>
            </form>

          </div>
        </div>
      )}

    </div>
  );
};

export default ApplicantInspectionPage;
