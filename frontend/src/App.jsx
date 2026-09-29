import { Routes, Route, Navigate } from 'react-router-dom'
import { useAuth } from './context/AuthContext'
import Navbar from './components/Navbar'
import Footer from './components/Footer'
import LandingPage from './pages/LandingPage'
import LoginPage from './pages/LoginPage'
import RegisterPage from './pages/RegisterPage'
import JobSearchPage from './pages/JobSearchPage'
import CandidateDashboardPage from './pages/CandidateDashboardPage'
import RecruiterDashboardPage from './pages/RecruiterDashboardPage'
import ResumeUploadPage from './pages/ResumeUploadPage'
import MyApplicationsPage from './pages/MyApplicationsPage'
import CompanyManagementPage from './pages/CompanyManagementPage'
import JobPostingPage from './pages/JobPostingPage'
import ApplicantInspectionPage from './pages/ApplicantInspectionPage'
import AICareerAssistantPage from './pages/AICareerAssistantPage'
import OAuth2RedirectHandler from './pages/OAuth2RedirectHandler'

function ProtectedRoute({ children }) {
  const { user, loading } = useAuth()
  if (loading) return null
  if (!user) return <Navigate to="/login" replace />
  return children
}

function App() {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', minHeight: '100vh' }}>
      <Navbar />
      <main style={{ flex: 1 }}>
        <Routes>
          <Route path="/" element={<LandingPage />} />
          <Route path="/login" element={<LoginPage />} />
          <Route path="/register" element={<RegisterPage />} />
          <Route path="/oauth2/redirect" element={<OAuth2RedirectHandler />} />
          <Route path="/jobs" element={<JobSearchPage />} />
          <Route path="/candidate/dashboard" element={<ProtectedRoute><CandidateDashboardPage /></ProtectedRoute>} />
          <Route path="/resumes" element={<ProtectedRoute><ResumeUploadPage /></ProtectedRoute>} />
          <Route path="/applications" element={<ProtectedRoute><MyApplicationsPage /></ProtectedRoute>} />
          <Route path="/ai-assistant" element={<ProtectedRoute><AICareerAssistantPage /></ProtectedRoute>} />
          <Route path="/recruiter/dashboard" element={<ProtectedRoute><RecruiterDashboardPage /></ProtectedRoute>} />
          <Route path="/recruiter/company" element={<ProtectedRoute><CompanyManagementPage /></ProtectedRoute>} />
          <Route path="/recruiter/post-job" element={<ProtectedRoute><JobPostingPage /></ProtectedRoute>} />
          <Route path="/recruiter/applicants" element={<ProtectedRoute><ApplicantInspectionPage /></ProtectedRoute>} />
          <Route path="*" element={<Navigate to="/" replace />} />
        </Routes>
      </main>
      <Footer />
    </div>
  )
}

export default App
