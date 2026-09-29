import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import api from '../api/axiosConfig';
import { Briefcase, CheckCircle, AlertCircle, PlusCircle } from 'lucide-react';

const JobPostingPage = () => {
  const { user } = useAuth();
  const navigate = useNavigate();

  const [title, setTitle] = useState('');
  const [description, setDescription] = useState('');
  const [requirements, setRequirements] = useState('');
  const [responsibilities, setResponsibilities] = useState('');
  const [location, setLocation] = useState('');
  const [salary, setSalary] = useState('');
  const [experienceRequired, setExperienceRequired] = useState('');
  const [jobType, setJobType] = useState('REMOTE');
  const [employmentType, setEmploymentType] = useState('FULL_TIME');
  const [applicationDeadline, setApplicationDeadline] = useState('');

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');

    const payload = {
      title,
      description,
      requirements,
      responsibilities,
      location,
      salary: salary ? Number(salary) : 100000,
      experienceRequired: experienceRequired ? Number(experienceRequired) : 2,
      jobType,
      employmentType,
      applicationDeadline: applicationDeadline ? new Date(applicationDeadline).toISOString() : null,
      recruiterId: user.id,
    };

    try {
      await api.post('/api/jobs', payload);
      navigate('/recruiter/dashboard');
    } catch (err) {
      console.error('Failed to post job:', err);
      setError(err.response?.data?.message || 'Failed to post new job opening.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={{ maxWidth: '760px', margin: '0 auto', padding: '32px 24px' }}>
      
      <div style={{ textAlign: 'center', marginBottom: '32px' }}>
        <h1 style={{ fontSize: '2.4rem', marginBottom: '8px' }}>
          Post a <span className="gradient-text">New Job</span>
        </h1>
        <p style={{ color: 'var(--text-secondary)' }}>
          Create a targeted tech job posting with automated candidate skill matching.
        </p>
      </div>

      <div className="glass-panel animate-fade-in" style={{ padding: '36px' }}>
        
        {error && (
          <div style={{ background: 'rgba(239, 68, 68, 0.15)', color: '#f87171', padding: '12px', borderRadius: '8px', marginBottom: '20px', display: 'flex', alignItems: 'center', gap: '8px', fontSize: '0.85rem' }}>
            <AlertCircle size={16} /> {error}
          </div>
        )}

        <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: '20px' }}>
          
          <div>
            <label style={{ fontSize: '0.8rem', fontWeight: 600, color: 'var(--text-secondary)', display: 'block', marginBottom: '6px' }}>
              Job Title *
            </label>
            <input 
              type="text" 
              className="input-field" 
              placeholder="e.g. Senior Full Stack Engineer (Spring Boot + React)"
              value={title}
              onChange={(e) => setTitle(e.target.value)}
              required
            />
          </div>

          <div>
            <label style={{ fontSize: '0.8rem', fontWeight: 600, color: 'var(--text-secondary)', display: 'block', marginBottom: '6px' }}>
              Job Description *
            </label>
            <textarea 
              className="input-field" 
              rows={4}
              placeholder="Detailed overview of the engineering role and project domain..."
              value={description}
              onChange={(e) => setDescription(e.target.value)}
              required
            />
          </div>

          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '16px' }}>
            <div>
              <label style={{ fontSize: '0.8rem', fontWeight: 600, color: 'var(--text-secondary)', display: 'block', marginBottom: '6px' }}>
                Requirements & Required Skills
              </label>
              <textarea 
                className="input-field" 
                rows={3}
                placeholder="e.g. Java, Spring Boot, MySQL, REST APIs, React"
                value={requirements}
                onChange={(e) => setRequirements(e.target.value)}
              />
            </div>

            <div>
              <label style={{ fontSize: '0.8rem', fontWeight: 600, color: 'var(--text-secondary)', display: 'block', marginBottom: '6px' }}>
                Responsibilities
              </label>
              <textarea 
                className="input-field" 
                rows={3}
                placeholder="e.g. Architect microservices, conduct code reviews"
                value={responsibilities}
                onChange={(e) => setResponsibilities(e.target.value)}
              />
            </div>
          </div>

          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr 1fr', gap: '16px' }}>
            <div>
              <label style={{ fontSize: '0.8rem', fontWeight: 600, color: 'var(--text-secondary)', display: 'block', marginBottom: '6px' }}>
                Location
              </label>
              <input 
                type="text" 
                className="input-field" 
                placeholder="e.g. Remote or Austin, TX"
                value={location}
                onChange={(e) => setLocation(e.target.value)}
              />
            </div>

            <div>
              <label style={{ fontSize: '0.8rem', fontWeight: 600, color: 'var(--text-secondary)', display: 'block', marginBottom: '6px' }}>
                Annual Salary ($ USD)
              </label>
              <input 
                type="number" 
                className="input-field" 
                placeholder="120000"
                value={salary}
                onChange={(e) => setSalary(e.target.value)}
              />
            </div>

            <div>
              <label style={{ fontSize: '0.8rem', fontWeight: 600, color: 'var(--text-secondary)', display: 'block', marginBottom: '6px' }}>
                Min Experience (Years)
              </label>
              <input 
                type="number" 
                className="input-field" 
                placeholder="3"
                value={experienceRequired}
                onChange={(e) => setExperienceRequired(e.target.value)}
              />
            </div>
          </div>

          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr 1fr', gap: '16px' }}>
            <div>
              <label style={{ fontSize: '0.8rem', fontWeight: 600, color: 'var(--text-secondary)', display: 'block', marginBottom: '6px' }}>
                Work Mode
              </label>
              <select className="input-field" value={jobType} onChange={(e) => setJobType(e.target.value)}>
                <option value="REMOTE">Remote</option>
                <option value="HYBRID">Hybrid</option>
                <option value="ON_SITE">On-Site</option>
              </select>
            </div>

            <div>
              <label style={{ fontSize: '0.8rem', fontWeight: 600, color: 'var(--text-secondary)', display: 'block', marginBottom: '6px' }}>
                Employment Type
              </label>
              <select className="input-field" value={employmentType} onChange={(e) => setEmploymentType(e.target.value)}>
                <option value="FULL_TIME">Full Time</option>
                <option value="PART_TIME">Part Time</option>
                <option value="CONTRACT">Contract</option>
                <option value="INTERNSHIP">Internship</option>
              </select>
            </div>

            <div>
              <label style={{ fontSize: '0.8rem', fontWeight: 600, color: 'var(--text-secondary)', display: 'block', marginBottom: '6px' }}>
                Application Deadline
              </label>
              <input 
                type="date" 
                className="input-field" 
                value={applicationDeadline}
                onChange={(e) => setApplicationDeadline(e.target.value)}
              />
            </div>
          </div>

          <button 
            type="submit" 
            className="btn-primary" 
            disabled={loading}
            style={{ width: '100%', justifyContent: 'center', padding: '12px', marginTop: '12px' }}
          >
            {loading ? 'Publishing Opening...' : <>Publish Job Opening <PlusCircle size={16} /></>}
          </button>

        </form>

      </div>

    </div>
  );
};

export default JobPostingPage;
