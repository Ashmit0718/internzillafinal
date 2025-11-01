import React, { useState } from 'react';
import { Link } from 'react-router-dom';

const LoginForm = ({ onLogin, setError, api }) => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [isLoading, setIsLoading] = useState(false);

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        setIsLoading(true);
        try {
            const data = await api.login(email, password);
            onLogin(data.jwt);
        } catch (err) {
            setError(err.message);
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <form onSubmit={handleSubmit}>
            <div className="form-group">
                <label htmlFor="login-email">Email Address</label>
                <input type="email" id="login-email" value={email} onChange={(e) => setEmail(e.target.value)} required />
            </div>
            <div className="form-group">
                <label htmlFor="login-password">Password</label>
                <input type="password" id="login-password" value={password} onChange={(e) => setPassword(e.target.value)} required />
            </div>
            <div className="form-group" style={{ textAlign: 'right', marginBottom: '1rem' }}>
                <Link to="/forgot-password" style={{ fontSize: '0.9rem' }}>Forgot Password?</Link>
            </div>
            <button type="submit" className="button auth-button" disabled={isLoading}>
                {isLoading ? 'Logging in...' : 'Login'}
            </button>
        </form>
    );
};

const RegisterForm = ({ onRegisterSuccess, setError, api }) => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [fullName, setFullName] = useState('');
    const [role, setRole] = useState('STUDENT');
    const [isLoading, setIsLoading] = useState(false);

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        setIsLoading(true);
        try {
            await api.register(fullName, email, password, role);
            onRegisterSuccess();
        } catch (err) {
            setError(err.message);
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <form onSubmit={handleSubmit}>
            <div className="form-group"><label htmlFor="reg-fullName">Full Name</label><input type="text" id="reg-fullName" value={fullName} onChange={(e) => setFullName(e.target.value)} required /></div>
            <div className="form-group"><label htmlFor="reg-email">Email Address</label><input type="email" id="reg-email" value={email} onChange={(e) => setEmail(e.target.value)} required /></div>
            <div className="form-group"><label htmlFor="reg-password">Password</label><input type="password" id="reg-password" value={password} onChange={(e) => setPassword(e.target.value)} required /></div>
            <div className="form-group"><label htmlFor="role">I am a:</label><select id="role" value={role} onChange={(e) => setRole(e.target.value)}><option value="STUDENT">Student</option><option value="RECRUITER">Recruiter</option></select></div>
            <button type="submit" className="button auth-button" disabled={isLoading}>{isLoading ? 'Signing up...' : 'Sign Up'}</button>
        </form>
    );
};

function AuthPage({ onLogin, api }) {
    const [isLogin, setIsLogin] = useState(true);
    const [error, setError] = useState('');
    const [success, setSuccess] = useState('');

    const handleRegisterSuccess = () => {
        setIsLogin(true);
        setSuccess('Registration successful! Please login.');
        setError('');
    };

    return (
        <div className="auth-page">
            <div className="auth-card">
                <h2 className="auth-title"><span className="logo-intern">Intern</span>Zilla</h2>
                <p className="auth-subtitle">{isLogin ? 'Welcome Back!' : 'Create Your Account'}</p>
                {error && <p className="error-message">{error}</p>}
                {success && <p className="success-message">{success}</p>}
                
                {isLogin 
                    ? <LoginForm onLogin={onLogin} setError={setError} api={api} /> 
                    : <RegisterForm onRegisterSuccess={handleRegisterSuccess} setError={setError} api={api} />
                }
                
                <p className="toggle-auth">
                    {isLogin ? "Don't have an account?" : 'Already have an account?'}
                    <button onClick={() => { setIsLogin(!isLogin); setError(''); setSuccess(''); }} className="toggle-auth-button">
                        {isLogin ? 'Sign Up' : 'Login'}
                    </button>
                </p>
            </div>
        </div>
    );
};

export default AuthPage;
