import React, { useState } from 'react';
import { Link } from 'react-router-dom';

// NOTE: The real api object is in App.jsx. This is a placeholder for now.
const api = {
    forgotPassword: (email) => fetch('http://localhost:8080/api/auth/forgot-password', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email }),
    }).then(res => {
        if (!res.ok) throw new Error('Failed to send reset link.');
        return res.json();
    })
};

function ForgotPasswordPage() {
    const [email, setEmail] = useState('');
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState('');
    const [success, setSuccess] = useState('');

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        setSuccess('');
        setIsLoading(true);
        try {
            const response = await api.forgotPassword(email);
            setSuccess(`If an account with that email exists, a password reset link has been generated. For this demo, the token is: ${response.token}` );
        } catch (err) {
            setError(err.message);
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <div className="auth-page">
            <div className="auth-card">
                <h2 className="auth-title">Forgot Password</h2>
                <p className="auth-subtitle">Enter your email to receive a reset link.</p>
                
                {error && <p className="error-message">{error}</p>}
                {success && <p className="success-message" style={{ whiteSpace: 'pre-wrap', wordBreak: 'break-all' }}>{success}</p>}

                {!success && (
                    <form onSubmit={handleSubmit}>
                        <div className="form-group">
                            <label htmlFor="forgot-email">Email Address</label>
                            <input
                                type="email"
                                id="forgot-email"
                                value={email}
                                onChange={(e) => setEmail(e.target.value)}
                                required
                            />
                        </div>
                        <button type="submit" className="button auth-button" disabled={isLoading}>
                            {isLoading ? 'Sending...' : 'Send Reset Link'}
                        </button>
                    </form>
                )}

                <div className="toggle-auth" style={{ marginTop: '1rem' }}>
                    <Link to="/login" className="toggle-auth-button">Back to Login</Link>
                </div>
            </div>
        </div>
    );
}

export default ForgotPasswordPage;
