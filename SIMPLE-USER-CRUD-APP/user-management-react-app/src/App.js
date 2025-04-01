import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import Navbar from './component/Navbar';
import HomePage from './component/HomePage';
import LoginForm from './component/LoginForm';
import AddUser from './component/AddUser';
import UserService from './service/UserService';

/**
 * The main application component.
 * This component handles routing, authentication, and rendering of different components based on the current route.
 *
 * @returns {JSX.Element} - The rendered React component.
 */
const App = () => {
    const [isAuthenticated, setIsAuthenticated] = useState(() => {
        // Initialize authentication state from localStorage
        const token = localStorage.getItem('token');
        return !!token; // Set to true if token exists, false otherwise
    });
    const [isContent, setIsContent] = useState();
    const [users, setUsers] = useState([]);
    const [totalPages, setTotalPages] = useState(0);

    useEffect(() => {
        const token = localStorage.getItem('token');
        if (token) {
            setIsAuthenticated(true);
        } else {
            setIsAuthenticated(false);
        }
    }, []);

    const handleSearch = async (query) => {
        if (isAuthenticated) {
            try {
                console.log(query);
                const data = await UserService.queryUsers(query);
                console.log(data);
                console.log(data.content);
                if (data.content.length === 0) {
                    setIsContent(false);
                } else {
                    setIsContent(true);
                    setUsers(data.content);
                    setTotalPages(data.totalPages);
                }
            } catch (error) {
                console.error('Error fetching queried users:', error);
            }
        }
    };

    return (
        <Router>
            <Navbar isAuthenticated={isAuthenticated} setIsAuthenticated={setIsAuthenticated} onSearch={handleSearch} />
            <Routes>
                {/* Public Routes */}
                <Route
                    path="/"
                    element={
                        <HomePage
                            isAuthenticated={isAuthenticated}
                            users={users}
                            totalPages={totalPages}
                            setUsers={setUsers}
                            setTotalPages={setTotalPages}
                            isContent={isContent}
                        />
                    }
                />
                <Route path="/login" element={<LoginForm setIsAuthenticated={setIsAuthenticated} />} />
                <Route path="/add-user" element={<AddUser />} />

                {/* Protected Route */}
                <Route
                    path="/home"
                    element={
                        isAuthenticated ? (
                            <HomePage
                                isAuthenticated={isAuthenticated}
                                users={users}
                                totalPages={totalPages}
                                setUsers={setUsers}
                                setTotalPages={setTotalPages}
                                isContent={isContent}
                            />
                        ) : (
                            <Navigate to="/login" />
                        )
                    }
                />
            </Routes>
        </Router>
    );
};

export default App;
