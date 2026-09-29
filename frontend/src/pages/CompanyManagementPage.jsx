import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import api from '../api/axiosConfig';
import { Building2, Save, MapPin, Globe, CheckCircle, AlertCircle } from 'lucide-react';

const CompanyManagementPage = () => {
  const { user } = useAuth();
  const navigate = useNavigate();

  const [companyId, setCompanyId] = useState(null);
  const [name, setName] = useState('');
  const [description, setDescription] = useState('');
  const [website, setWebsite] = useState('');
  const [logoUrl, setLogoUrl] = useState('');
  const [industry, setIndustry] = useState('');
  const [companySize, setCompanySize] = useState('');
  const [location, setLocation] = useState('');

  const [loading, setLoading] = useState(false);
  const [success, setSuccess] = useState(false);
  const [error, setError] = useState('');

  useEffect(() => {
    if (user && user.id) {
      loadCompany();
    }
  }, [user]);

  const loadCompany = async () => {
    try {
      const recRes = await api.get(`/api/recruiters/${user.id}`);
      if (recRes.data.companyId) {
        setCompanyId(recRes.data.companyId);
        const compRes = await api.get(`/api/companies/${recRes.data.companyId}`);
        const c = compRes.data;
        setName(c.name || '');
        setDescription(c.description || '');
        setWebsite(c.website || '');
        setLogoUrl(c.logoUrl || '');
        setIndustry(c.industry || '');
        setCompanySize(c.companySize || '');
        setLocation(c.location || '');
      }
    } catch (err) {
      console.error('Failed to load company details:', err);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');
    setSuccess(false);

    const payload = {
      name,
      description,
      website,
      logoUrl,
      industry,
      companySize,
      location,
      recruiterId: user.id,
    };

    try {
      if (companyId) {
        await api.put(`/api/companies/${companyId}`, payload);
      } else {
        const res = await api.post('/api/companies', payload);
        setCompanyId(res.data.id);
      }
      setSuccess(true);
      setTimeout(() => setSuccess(false), 3000);
    } catch (err) {
      console.error('Failed to save company profile:', err);
      setError(err.response?.data?.message || 'Failed to save company details.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={{ maxWidth: '720px', margin: '0 auto', padding: '32px 24px' }}>
      
      <div style={{ textAlign: 'center', marginBottom: '32px' }}>
        <h1 style={{ fontSize: '2.4rem', marginBottom: '8px' }}>
          Manage <span className="gradient-text">Company Profile</span>
        </h1>
        <p style={{ color: 'var(--text-secondary)' }}>
          Setup your organization details to showcase your brand to top tech talent.
        </p>
      </div>

      <div className="glass-panel animate-fade-in" style={{ padding: '36px' }}>
        
        {success && (
          <div style={{ background: 'rgba(16, 185, 129, 0.15)', color: '#34d399', padding: '12px', borderRadius: '8px', marginBottom: '20px', display: 'flex', alignItems: 'center', gap: '8px', fontSize: '0.85rem' }}>
            <CheckCircle size={16} /> Company profile saved successfully!
          </div>
        )}

        {error && (
          <div style={{ background: 'rgba(239, 68, 68, 0.15)', color: '#f87171', padding: '12px', borderRadius: '8px', marginBottom: '20px', display: 'flex', alignItems: 'center', gap: '8px', fontSize: '0.85rem' }}>
            <AlertCircle size={16} /> {error}
          </div>
        )}

        <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: '20px' }}>
          
          <div>
            <label style={{ fontSize: '0.8rem', fontWeight: 600, color: 'var(--text-secondary)', display: 'block', marginBottom: '6px' }}>
              Company Name *
            </label>
            <input 
              type="text" 
              className="input-field" 
              placeholder="e.g. Acme Tech Solutions"
              value={name}
              onChange={(e) => setName(e.target.value)}
              required
            />
          </div>

          <div>
            <label style={{ fontSize: '0.8rem', fontWeight: 600, color: 'var(--text-secondary)', display: 'block', marginBottom: '6px' }}>
              Description & Mission
            </label>
            <textarea 
              className="input-field" 
              rows={4}
              placeholder="Tell candidates about your company engineering culture..."
              value={description}
              onChange={(e) => setDescription(e.target.value)}
            />
          </div>

          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '16px' }}>
            <div>
              <label style={{ fontSize: '0.8rem', fontWeight: 600, color: 'var(--text-secondary)', display: 'block', marginBottom: '6px' }}>
                Website URL
              </label>
              <input 
                type="url" 
                className="input-field" 
                placeholder="https://company.com"
                value={website}
                onChange={(e) => setWebsite(e.target.value)}
              />
            </div>

            <div>
              <label style={{ fontSize: '0.8rem', fontWeight: 600, color: 'var(--text-secondary)', display: 'block', marginBottom: '6px' }}>
                Logo Image URL
              </label>
              <input 
                type="text" 
                className="input-field" 
                placeholder="https://company.com/logo.png"
                value={logoUrl}
                onChange={(e) => setLogoUrl(e.target.value)}
              />
            </div>
          </div>

          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr 1fr', gap: '16px' }}>
            <div>
              <label style={{ fontSize: '0.8rem', fontWeight: 600, color: 'var(--text-secondary)', display: 'block', marginBottom: '6px' }}>
                Industry
              </label>
              <input 
                type="text" 
                className="input-field" 
                placeholder="e.g. Software, Fintech"
                value={industry}
                onChange={(e) => setIndustry(e.target.value)}
              />
            </div>

            <div>
              <label style={{ fontSize: '0.8rem', fontWeight: 600, color: 'var(--text-secondary)', display: 'block', marginBottom: '6px' }}>
                Company Size
              </label>
              <input 
                type="text" 
                className="input-field" 
                placeholder="e.g. 50-200"
                value={companySize}
                onChange={(e) => setCompanySize(e.target.value)}
              />
            </div>

            <div>
              <label style={{ fontSize: '0.8rem', fontWeight: 600, color: 'var(--text-secondary)', display: 'block', marginBottom: '6px' }}>
                Headquarters Location
              </label>
              <input 
                type="text" 
                className="input-field" 
                placeholder="e.g. San Francisco, CA"
                value={location}
                onChange={(e) => setLocation(e.target.value)}
              />
            </div>
          </div>

          <button 
            type="submit" 
            className="btn-primary" 
            disabled={loading}
            style={{ width: '100%', justifyContent: 'center', padding: '12px', marginTop: '12px' }}
          >
            {loading ? 'Saving Profile...' : <>Save Company Profile <Save size={16} /></>}
          </button>

        </form>

      </div>

    </div>
  );
};

export default CompanyManagementPage;
