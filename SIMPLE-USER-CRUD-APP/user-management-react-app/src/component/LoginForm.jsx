import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Box, TextField, Button, Snackbar, Alert } from '@mui/material';
import UserService from '../service/UserService';

/**
 * The LoginForm component is a React functional component that renders a login form for user authentication.
 * It interacts with the backend API through the `UserService` to validate user credentials and retrieve a token.
 * The token is stored in `localStorage` to facilitate authenticated API calls for the session.
 * 
 * Props:
 * - `setIsAuthenticated (function)`: A function passed from the parent component to update the authenticated state of the application.
 * 
 * State:
 * - `credentials (object)`: Stores the username and password entered by the user.
 *   - `userName (string)`: The username input by the user.
 *   - `passWord (string)`: The password input by the user.
 * - `error (string)`: Stores error messages related to failed login attempts.
 * 
 * Functions:
 * - `handleSubmit (async)`: Handles the form submission, sends the credentials to the backend via `UserService.login`, 
 *   stores the JWT token upon success, and navigates the user to the home page. Displays an error message if login fails.
 * 
 * UI Elements:
 * - `TextField`: Material-UI component used for user input fields (Username and Password).
 * - `Button`: Material-UI button for submitting the form.
 * - `Snackbar` & `Alert`: Material-UI components used to display error messages when login fails.
 * 
 * Material-UI Styles:
 * - The form is centered horizontally using `mx: 'auto'` and padded with `p: 3`.
 * - Maximum width is limited to 400px for better layout on larger screens.
 * - Buttons and fields have consistent margins for a clean layout.
 * 
 * Workflow:
 * 1. The user enters their username and password.
 * 2. On form submission, the `handleSubmit` function sends the credentials to the backend.
 * 3. If login is successful:
 *    - The JWT token is stored in `localStorage`.
 *    - The `setIsAuthenticated` function is called with `true`.
 *    - The user is redirected to the `/home` page.
 * 4. If login fails:
 *    - An error message is displayed in a `Snackbar` with an `Alert` component.
 * 
 * Dependencies:
 * - React: For state management and component lifecycle.
 * - React Router (`useNavigate`): For navigation after successful login.
 * - Material-UI: For UI components like `TextField`, `Button`, `Snackbar`, and `Alert`.
 * - UserService: Custom service module used to interact with the backend login API.
 * 
 * Example Usage:
 * ```
 * <LoginForm setIsAuthenticated={setIsAuthenticated} />
 * ```
 * 
 * Notes:
 * - The component assumes that `UserService.login` returns a JWT token upon successful login.
 * - `localStorage` is used to store the token for session persistence, but it can be replaced with other storage mechanisms if needed.
 */

const LoginForm = ({ setIsAuthenticated }) => {
    const [credentials, setCredentials] = useState({ userName: '', passWord: '' });
    const [error, setError] = useState('');
    const [errors, setErrors] = useState({});
    const navigate = useNavigate();

    const validate = () => {
        const newErrors = {};

        if (!credentials.userName.trim()) newErrors.userName = 'Username is required.';
        if (!credentials.passWord.trim()) newErrors.passWord = 'Password is required.';
        else if (credentials.passWord.length < 8) newErrors.passWord = 'Password must be at least 6 characters long.';

        setErrors(newErrors);
        return Object.keys(newErrors).length === 0; // Form is valid if no errors
    };
    
    const handleSubmit = async (e) => {
        e.preventDefault();
        if (!validate()) return;
        try {
            const token = await UserService.login(credentials);
            if (!token) {
                throw new Error('Token not received.');
            }
            // console.log(token);
            localStorage.setItem('token', token);
            setIsAuthenticated(true);
            navigate('/home');
        } catch (err) {
            setError('Login failed. Please check your credentials.');
        }
    };

    return (
        <Box sx={{ p: 3, maxWidth: 400, mx: 'auto' }}>
            <form onSubmit={handleSubmit}>
                <TextField
                    label="Username"
                    fullWidth
                    margin="normal"
                    value={credentials.userName}
                    onChange={(e) => setCredentials({ ...credentials, userName: e.target.value })}
                    error={!!errors.userName}
                    helperText={errors.userName}
                />
                <TextField
                    label="Password"
                    type="password"
                    fullWidth
                    margin="normal"
                    value={credentials.passWord}
                    onChange={(e) => setCredentials({ ...credentials, passWord: e.target.value })}
                    error={!!errors.passWord}
                    helperText={errors.passWord}
                />
                <Button type="submit" variant="contained" fullWidth sx={{ mt: 2 }}>
                    Login
                </Button>
            </form>
            {error && (
                <Snackbar open autoHideDuration={6000} onClose={() => setError('')}>
                    <Alert severity="error">{error}</Alert>
                </Snackbar>
            )}
        </Box>
    );
};

export default LoginForm;
