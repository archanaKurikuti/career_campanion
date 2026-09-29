import React, { useState, useEffect } from 'react';
import api from '../api/axiosConfig';
import { useAuth } from '../context/AuthContext';
import { 
  Search, 
  MapPin, 
  DollarSign, 
  Briefcase, 
  Filter, 
  Sparkles, 
  Send, 
  CheckCircle2, 
  X, 
  Building2, 
  Clock, 
  Award,
  ChevronLeft,
  ChevronRight
} from 'lucide-react';

const JobSearchPage = () => {
  const { user, role } = useAuth();

  const [jobs, setJobs] = useState([]);
  const [loading, setLoading] = useState(true);
  const [totalPages, setTotalPages] = useState(0);
  const [currentPage, setCurrentPage] = useState(0);

  // Filters
  const [keyword, setKeyword] = useState('');
  const [location, setLocation] = useState('');
  const [minSalary, setMinSalary] = useState('');
  const [jobType, setJobType] = useState('');
  const [employmentType, setEmploymentType] = useState('');

  // Selected Job & Application Modal
  const [selectedJob, setSelectedJob] = useState(null);
  const [coverLetter, setCoverLetter] = useState('');
  const [applyLoading, setApplyLoading] = useState(false);
  const [applySuccess, setApplySuccess] = useState(false);
  const [applyError, setApplyError] = useState('');

  // AI Match Score State
  const [matchScoreMap, setMatchScoreMap] = useState({});
  const [matchingJobId, setMatchingJobId] = useState(null);

  useEffect(() => {
    fetchJobs(0);
  }, []);

  const fetchJobs = async (page = 0) => {
    setLoading(true);
    try {
      const params = {
        page,
        size: 6,
        keyword: keyword || undefined,
        location: location || undefined,
        minSalary: minSalary ? Number(minSalary) : undefined,
        jobType: jobType || undefined,
        employmentType: employmentType || undefined,
      };

      const res = await api.get('/api/jobs', { params });
      setJobs(res.data.content || []);
      setTotalPages(res.data.totalPages || 1);
      setCurrentPage(page);
    } catch (err) {
      console.error('Failed to fetch jobs:', err);
    } finally {
      setLoading(false);
    }
  };

  const handleSearch = (e) => {
    e.preventDefault();
    fetchJobs(0);
  };

  const handleCalculateMatch = async (jobId) => {
    if (!user || role !== 'CANDIDATE') return;
    setMatchingJobId(jobId);
    try {
      const res = await api.get(`/api/ai/match?candidateId=${user.id}&jobId=${jobId}`);
      setMatchScoreMap(prev => ({
        ...prev,
        [jobId]: res.data
      }));
    } catch (err) {
      console.error('Failed to calculate match score:', err);
    } finally {
      setMatchingJobId(null);
    }
  };

  const handleApplySubmit = async (e) => {
    e.preventDefault();
    if (!selectedJob || !user) return;
    setApplyLoading(true);
    setApplyError('');
    setApplySuccess(false);

    try {
      await api.post(`/api/applications/candidates/${user.id}`, {
        jobId: selectedJob.id,
        candidateId: user.id,
        coverLetter,
      });
      setApplySuccess(true);
      setTimeout(() => {
        setSelectedJob(null);
        setApplySuccess(false);
        setCoverLetter('');
      }, 2000);
    } catch (err) {
      console.error('Failed to submit application:', err);
      setApplyError(err.response?.data?.message || 'Failed to submit job application.');
    } finally {
      setApplyLoading(false);
    }
  };

  return (
    <div style={{ maxWidth: '1280px', margin: '0 auto', padding: '32px 24px' }}>
      
      {/* Header */}
      <div style={{ textAlign: 'center', marginBottom: '32px' }}>
        <h1 style={{ fontSize: '2.4rem', marginBottom: '8px' }}>
          Explore Open <span className="gradient-text">Opportunities</span>
        </h1>
        <p style={{ color: 'var(--text-secondary)' }}>
          Discover top engineering and tech roles with instant AI match scoring.
        </p>
      </div>

      {/* Filter Bar Form */}
      <form onSubmit={handleSearch} className="glass-panel" style={{ padding: '20px', marginBottom: '40px' }}>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(180px, 1fr))', gap: '16px', alignItems: 'end' }}>
          
          <div>
            <label style={{ fontSize: '0.75rem', fontWeight: 600, color: 'var(--text-secondary)', display: 'block', marginBottom: '6px' }}>
              Keywords
            </label>
            <div style={{ position: 'relative' }}>
              <input 
                type="text" 
                className="input-field" 
                placeholder="Job title or skills..."
                value={keyword}
                onChange={(e) => setKeyword(e.target.value)}
                style={{ paddingLeft: '36px' }}
              />
              <Search size={16} color="var(--text-muted)" style={{ position: 'absolute', left: '12px', top: '12px' }} />
            </div>
          </div>

          <div>
            <label style={{ fontSize: '0.75rem', fontWeight: 600, color: 'var(--text-secondary)', display: 'block', marginBottom: '6px' }}>
              Location
            </label>
            <div style={{ position: 'relative' }}>
              <input 
                type="text" 
                className="input-field" 
                placeholder="City or Remote..."
                value={location}
                onChange={(e) => setLocation(e.target.value)}
                style={{ paddingLeft: '36px' }}
              />
              <MapPin size={16} color="var(--text-muted)" style={{ position: 'absolute', left: '12px', top: '12px' }} />
            </div>
          </div>

          <div>
            <label style={{ fontSize: '0.75rem', fontWeight: 600, color: 'var(--text-secondary)', display: 'block', marginBottom: '6px' }}>
              Job Type
            </label>
            <select 
              className="input-field" 
              value={jobType} 
              onChange={(e) => setJobType(e.target.value)}
            >
              <option value="">All Job Types</option>
              <option value="REMOTE">Remote</option>
              <option value="HYBRID">Hybrid</option>
              <option value="ON_SITE">On-Site</option>
            </select>
          </div>

          <div>
            <label style={{ fontSize: '0.75rem', fontWeight: 600, color: 'var(--text-secondary)', display: 'block', marginBottom: '6px' }}>
              Employment Type
            </label>
            <select 
              className="input-field" 
              value={employmentType} 
              onChange={(e) => setEmploymentType(e.target.value)}
            >
              <option value="">All Employment</option>
              <option value="FULL_TIME">Full Time</option>
              <option value="PART_TIME">Part Time</option>
              <option value="CONTRACT">Contract</option>
              <option value="INTERNSHIP">Internship</option>
            </select>
          </div>

          <div>
            <button type="submit" className="btn-primary" style={{ width: '100%', justifyContent: 'center', padding: '12px' }}>
              <Filter size={16} /> Filter Jobs
            </button>
          </div>

        </div>
      </form>

      {/* Jobs Grid */}
      {loading ? (
        <div style={{ textAlign: 'center', padding: '60px 0', color: 'var(--text-secondary)' }}>
          Loading job listings...
        </div>
      ) : jobs.length === 0 ? (
        <div className="glass-panel" style={{ padding: '60px', textAlign: 'center' }}>
          <Briefcase size={40} color="var(--text-muted)" style={{ marginBottom: '16px' }} />
          <h3>No jobs found</h3>
          <p style={{ color: 'var(--text-secondary)', fontSize: '0.9rem', marginTop: '6px' }}>
            Try adjusting your search keywords or location filter.
          </p>
        </div>
      ) : (
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(360px, 1fr))', gap: '24px' }}>
          {jobs.map(job => {
            const matchInfo = matchScoreMap[job.id];
            return (
              <div key={job.id} className="glass-panel glass-panel-hover" style={{ padding: '24px', display: 'flex', flexDirection: 'column', justifyContent: 'space-between' }}>
                <div>
                  <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', marginBottom: '12px' }}>
                    <div>
                      <h3 style={{ fontSize: '1.2rem', marginBottom: '4px' }}>{job.title}</h3>
                      <p style={{ fontSize: '0.85rem', color: 'var(--text-secondary)', display: 'flex', alignItems: 'center', gap: '6px' }}>
                        <Building2 size={14} color="var(--accent-cyan)" /> {job.companyName || 'Tech Partner'}
                      </p>
                    </div>
                    <span className="badge badge-candidate" style={{ fontSize: '0.65rem' }}>
                      {job.jobType || 'FULL TIME'}
                    </span>
                  </div>

                  <div style={{ display: 'flex', gap: '16px', flexWrap: 'wrap', fontSize: '0.8rem', color: 'var(--text-muted)', marginBottom: '16px' }}>
                    <span style={{ display: 'flex', alignItems: 'center', gap: '4px' }}>
                      <MapPin size={14} /> {job.location || 'Remote'}
                    </span>
                    <span style={{ display: 'flex', alignItems: 'center', gap: '4px' }}>
                      <DollarSign size={14} /> ${job.salary ? job.salary.toLocaleString() : '100,000'}/yr
                    </span>
                    <span style={{ display: 'flex', alignItems: 'center', gap: '4px' }}>
                      <Award size={14} /> {job.experienceRequired || 0}+ yrs exp
                    </span>
                  </div>

                  <p style={{ fontSize: '0.85rem', color: 'var(--text-secondary)', lineHeight: 1.5, marginBottom: '20px', display: '-webkit-box', WebkitLineClamp: 3, WebkitBoxOrient: 'vertical', overflow: 'hidden' }}>
                    {job.description}
                  </p>

                  {/* AI Match Gauge */}
                  {matchInfo && (
                    <div style={{ 
                      background: 'rgba(6, 182, 212, 0.1)', 
                      border: '1px solid rgba(6, 182, 212, 0.3)', 
                      borderRadius: '10px', 
                      padding: '10px 14px', 
                      marginBottom: '16px' 
                    }}>
                      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '4px' }}>
                        <span style={{ fontSize: '0.75rem', fontWeight: 700, color: 'var(--accent-cyan)', display: 'flex', alignItems: 'center', gap: '4px' }}>
                          <Sparkles size={14} /> AI Match Score
                        </span>
                        <span style={{ fontSize: '0.9rem', fontWeight: 800, color: 'white' }}>
                          {matchInfo.matchScore}%
                        </span>
                      </div>
                      <p style={{ fontSize: '0.75rem', color: 'var(--text-secondary)' }}>
                        {matchInfo.matchAnalysis}
                      </p>
                    </div>
                  )}
                </div>

                <div style={{ display: 'flex', gap: '10px', marginTop: '12px' }}>
                  {user && role === 'CANDIDATE' && (
                    <>
                      <button 
                        onClick={() => handleCalculateMatch(job.id)}
                        disabled={matchingJobId === job.id}
                        className="btn-secondary" 
                        style={{ fontSize: '0.8rem', padding: '8px 12px' }}
                      >
                        <Sparkles size={14} color="var(--accent-purple)" />
                        {matchingJobId === job.id ? 'Scoring...' : 'AI Match'}
                      </button>

                      <button 
                        onClick={() => setSelectedJob(job)}
                        className="btn-primary" 
                        style={{ fontSize: '0.8rem', padding: '8px 16px', flex: 1, justifyContent: 'center' }}
                      >
                        Apply Now
                      </button>
                    </>
                  )}

                  {(!user || role !== 'CANDIDATE') && (
                    <button 
                      onClick={() => setSelectedJob(job)}
                      className="btn-secondary" 
                      style={{ fontSize: '0.8rem', width: '100%', justifyContent: 'center' }}
                    >
                      View Details
                    </button>
                  )}
                </div>

              </div>
            );
          })}
        </div>
      )}

      {/* Pagination */}
      {totalPages > 1 && (
        <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', gap: '16px', marginTop: '40px' }}>
          <button 
            disabled={currentPage === 0}
            onClick={() => fetchJobs(currentPage - 1)}
            className="btn-secondary"
            style={{ opacity: currentPage === 0 ? 0.5 : 1 }}
          >
            <ChevronLeft size={16} /> Prev
          </button>
          <span style={{ fontSize: '0.9rem', color: 'var(--text-secondary)' }}>
            Page {currentPage + 1} of {totalPages}
          </span>
          <button 
            disabled={currentPage >= totalPages - 1}
            onClick={() => fetchJobs(currentPage + 1)}
            className="btn-secondary"
            style={{ opacity: currentPage >= totalPages - 1 ? 0.5 : 1 }}
          >
            Next <ChevronRight size={16} />
          </button>
        </div>
      )}

      {/* Application Modal */}
      {selectedJob && (
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
          <div className="glass-panel animate-fade-in" style={{ maxWidth: '540px', width: '100%', padding: '32px', position: 'relative' }}>
            
            <button 
              onClick={() => setSelectedJob(null)}
              style={{ position: 'absolute', right: '20px', top: '20px', background: 'transparent', color: 'var(--text-muted)' }}
            >
              <X size={20} />
            </button>

            <h3 style={{ fontSize: '1.4rem', marginBottom: '4px' }}>Apply for {selectedJob.title}</h3>
            <p style={{ fontSize: '0.85rem', color: 'var(--text-secondary)', marginBottom: '20px' }}>
              {selectedJob.companyName} • {selectedJob.location || 'Remote'}
            </p>

            {applySuccess ? (
              <div style={{ textAlign: 'center', padding: '32px 0' }}>
                <CheckCircle2 size={48} color="#10b981" style={{ marginBottom: '12px' }} />
                <h4 style={{ fontSize: '1.2rem', color: '#34d399' }}>Application Submitted!</h4>
                <p style={{ fontSize: '0.85rem', color: 'var(--text-secondary)', marginTop: '4px' }}>
                  The recruiter has been notified of your profile.
                </p>
              </div>
            ) : (
              <form onSubmit={handleApplySubmit} style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
                {applyError && (
                  <div style={{ background: 'rgba(239, 68, 68, 0.15)', color: '#f87171', padding: '10px', borderRadius: '8px', fontSize: '0.85rem' }}>
                    {applyError}
                  </div>
                )}

                <div>
                  <label style={{ fontSize: '0.8rem', fontWeight: 600, color: 'var(--text-secondary)', display: 'block', marginBottom: '6px' }}>
                    Cover Letter / Introduction
                  </label>
                  <textarea 
                    className="input-field" 
                    rows={5} 
                    placeholder="Describe why you are a great fit for this position..."
                    value={coverLetter}
                    onChange={(e) => setCoverLetter(e.target.value)}
                    required
                  />
                </div>

                <button 
                  type="submit" 
                  className="btn-primary" 
                  disabled={applyLoading || !user}
                  style={{ width: '100%', justifyContent: 'center', padding: '12px' }}
                >
                  {applyLoading ? 'Submitting...' : <>Submit Application <Send size={16} /></>}
                </button>
              </form>
            )}

          </div>
        </div>
      )}

    </div>
  );
};

export default JobSearchPage;
