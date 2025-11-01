import React, { useState, useEffect } from 'react';
import { Link, useSearchParams } from 'react-router-dom';

// NOTE: The real api object is in App.jsx. This is a placeholder for now.
const api = {
    resetPassword: (token, newPassword) => fetch('http://localhost:8080/api/auth/reset-password', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ token, newPassword }),
    }).then(res => {
        if (!res.ok) throw new Error('Failed to reset password. The token may be invalid.');
        return res.json();
    })
};

function ResetPasswordPage() {
    const [searchParams] = useSearchParams();
    const [token, setToken] = useState(null);
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState('');
    const [success, setSuccess] = useState('');

    useEffect(() => {
        const tokenFromUrl = searchParams.get('token');
        if (tokenFromUrl) {
            setToken(tokenFromUrl);
        } else {
            setError('No reset token found in URL. Please use the link from your email.');
        }
    }, [searchParams]);

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (password.length < 8) {
            setError('Password must be at least 8 characters long.');
            return;
        }
        if (password !== confirmPassword) {
            setError('Passwords do not match.');
            return;
        }
        setError('');
        setSuccess('');
        setIsLoading(true);
        try {
            await api.resetPassword(token, password);
            setSuccess('Your password has been reset successfully!');
        } catch (err) {
            setError(err.message);
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <div className="auth-page">
            <div className="auth-card">
                <h2 className="auth-title">Reset Your Password</h2>
                
                {error && <p className="error-message">{error}</p>}
                {success && <p className="success-message">{success}</p>}

                {!success && token && (
                    <form onSubmit={handleSubmit}>
                        <div className="form-group">
                            <label htmlFor="new-password">New Password (min. 8 characters)</label>
                            <input
                                type="password"
                                id="new-password"
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                                required
                            />
                        </div>
                        <div className="form-group">
                            <label htmlFor="confirm-password">Confirm New Password</label>
                            <input
                                type="password"
                                id="confirm-password"
                                value={confirmPassword}
                                onChange={(e) => setConfirmPassword(e.target.value)}
                                required
                            />
                        </div>
                        <button type="submit" className="button auth-button" disabled={isLoading}>
                            {isLoading ? 'Resetting...' : 'Reset Password'}
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

export default ResetPasswordPage;
