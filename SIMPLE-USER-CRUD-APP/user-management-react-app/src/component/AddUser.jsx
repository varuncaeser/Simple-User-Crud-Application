import React, { useState } from 'react';
import { Box, TextField, Button, MenuItem, Select, InputLabel, FormControl, Snackbar, Alert } from '@mui/material';
// import { useNavigate } from 'react-router-dom';
import UserService from '../service/UserService';

/**
 * A functional component for adding a new user to the system.
 * It includes form fields for user details and a role selection dropdown.
 * The form data is handled by the useState hook and submitted to the UserService.
 * Displays a Snackbar with success or error message based on the user creation outcome.
 *
 * @returns {JSX.Element} - The JSX representation of the AddUser component.
 */
const AddUser = () => {
    const [userData, setUserData] = useState({
        userName: '',
        firstName: '',
        lastName: '',
        email: '',
        passWord: '',
        roles: 'ROLE_USER', // Default to 'ROLE_USER'
    });
    const [errors, setErrors] = useState({});
    const [error, setError] = useState('');
    // const navigate = useNavigate();

    /**
     * Handles form input changes and updates the userData state.
     *
     * @param {React.ChangeEvent<HTMLInputElement>} e - The event object.
     */
    const handleChange = (e) => {
        setUserData({
            ...userData,
            [e.target.name]: e.target.value,
        });

        setErrors((prev) => ({
            ...prev,
            [e.target.name]: '',
        }));
    };

    /**
     * Validates the form fields and returns true if all are valid.
     * @returns {boolean} - Validation status.
     */
    const validate = () => {
        const newErrors = {};

        if (!userData.userName.trim()) newErrors.userName = 'Username is required.';
        if (!userData.firstName.trim()) newErrors.firstName = 'First Name is required.';
        if (!userData.lastName.trim()) newErrors.lastName = 'Last Name is required.';
        if (!userData.email.trim()) newErrors.email = 'Email is required.';
        else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(userData.email)) newErrors.email = 'Invalid email format.';
        if (!userData.passWord.trim()) newErrors.passWord = 'Password is required.';
        else if (userData.passWord.length < 8) newErrors.passWord = 'Password must be at least 6 characters long.';

        setErrors(newErrors);
        return Object.keys(newErrors).length === 0; // Form is valid if no errors
    };

    /**
    * Handles form submission by validating and calling the UserService to add a new user.
    * Displays success or error message based on the outcome.
    *
    * @param {React.FormEvent<HTMLFormElement>} e - The event object.
    */
    const handleSubmit = async (e) => {
        e.preventDefault();
        if (!validate()) return;

        try {
            await UserService.addNewUser(userData);
            setError('User created successfully!'); // Set success message for Snackbar
            setUserData({
                userName: '',
                firstName: '',
                lastName: '',
                email: '',
                passWord: '',
                roles: 'ROLE_USER',
            }); // Reset form
        } catch (err) {
            setError('User creation failed. Please try again.');
        }
    };

    return (
        <Box sx={{ p: 3, maxWidth: 800, mx: 'auto' }}>
            <form onSubmit={handleSubmit}>
                <TextField
                    label="Username"
                    name="userName"
                    fullWidth
                    margin="normal"
                    value={userData.userName}
                    onChange={handleChange}
                    error={!!errors.userName}
                    helperText={errors.userName}
                />
                <TextField
                    label="First Name"
                    name="firstName"
                    fullWidth
                    margin="normal"
                    value={userData.firstName}
                    onChange={handleChange}
                    error={!!errors.firstName}
                    helperText={errors.firstName}
                />
                <TextField
                    label="Last Name"
                    name="lastName"
                    fullWidth
                    margin="normal"
                    value={userData.lastName}
                    onChange={handleChange}
                    error={!!errors.lastName}
                    helperText={errors.lastName}
                />
                <TextField
                    label="Email"
                    name="email"
                    fullWidth
                    margin="normal"
                    value={userData.email}
                    onChange={handleChange}
                    error={!!errors.email}
                    helperText={errors.email}
                />
                <TextField
                    label="Password"
                    name="passWord"
                    type="password"
                    fullWidth
                    margin="normal"
                    value={userData.passWord}
                    onChange={handleChange}
                    error={!!errors.passWord}
                    helperText={errors.passWord}
                />
                <FormControl fullWidth margin="normal">
                    <InputLabel>Role</InputLabel>
                    <Select
                        name="roles"
                        value={userData.roles}
                        onChange={handleChange}
                    >
                        <MenuItem value="ROLE_USER">User</MenuItem>
                        <MenuItem value="ROLE_ADMIN">Admin</MenuItem>
                    </Select>
                </FormControl>
                <Button
                    type="submit"
                    variant="contained"
                    fullWidth
                    sx={{ mt: 2 }}
                >
                    {userData.roles === 'ROLE_USER' ? 'Add User' : 'Add Admin'}
                </Button>
            </form>
            {error && (
                <Snackbar
                    open={!!error}
                    autoHideDuration={6000}
                    onClose={() => setError('')}
                >
                    <Alert severity={error === 'User created successfully!' ? 'success' : 'error'}>{error}</Alert>
                </Snackbar>
            )}
        </Box>
    );
};

export default AddUser;