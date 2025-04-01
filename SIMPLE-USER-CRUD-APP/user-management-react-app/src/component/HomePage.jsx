import React, { useEffect, useState } from "react";
import {
    Table,
    TableBody,
    TableCell,
    TableHead,
    TableRow,
    Pagination,
    Typography,
    Box,
} from "@mui/material";
import MenuIcon from "@mui/icons-material/Menu";
import UserService from "../service/UserService";

/**
 * The HomePage component fetches and displays a list of users by default.
 * It handles search results, displaying updated user data or a "NO RESULTS FOR SEARCH" message.
 *
 * @param {Object} props - The component's props
 * @param {boolean} props.isAuthenticated - Indicates whether the user is authenticated.
 * @param {boolean} props.isContent - Indicates if there is valid content from a search.
 * @param {Array} props.users - The list of users to display.
 * @param {number} props.totalPages - Total number of pages for pagination.
 * @param {Function} props.setUsers - Function to update the user list.
 * @param {Function} props.setTotalPages - Function to update the total number of pages.
 *
 * @returns {JSX.Element} - The rendered HomePage component.
 */
const HomePage = ({
    isAuthenticated,
    isContent,
    users,
    totalPages,
    setUsers,
    setTotalPages,
}) => {
    const [page, setPage] = useState(1);
    const [size] = useState(10); // Default page size

    // Fetch users on component mount or when the page changes
    useEffect(() => {
        const fetchUsers = async () => {
            if (isAuthenticated) {
                try {
                    const data = await UserService.getUsers(page - 1, size); // API expects 0-based index
                    setUsers(data.content);
                    setTotalPages(data.totalPages);
                } catch (err) {
                    console.error("Error fetching users:", err);
                }
            }
        };

        if (!isContent) fetchUsers();
    }, [isAuthenticated, page, size, isContent, setUsers, setTotalPages]);

    // Handle page change for pagination
    const handlePageChange = async (event, newPage) => {
        setPage(newPage);
        try {
            const data = await UserService.getUsers(newPage - 1, size);
            setUsers(data.content);
            setTotalPages(data.totalPages);
        } catch (err) {
            console.error("Error fetching users:", err);
        }
    };

    return (
        <Box sx={{ p: 3 }}>
            {isAuthenticated ? (
                <>
                    {isContent === false ? (
                        // No results found
                        <>
                            <Typography
                                sx={{
                                    opacity: 0.5,
                                    textAlign: "center",
                                    mt: 5,
                                    display: "flex",
                                    alignItems: "center",
                                    justifyContent: "center",
                                }}
                            >
                                NO RESULTS FOR SEARCH
                            </Typography>
                        </>
                    ) : (
                        // Display user table
                        <>
                            <Table>
                                <TableHead>
                                    <TableRow>
                                        <TableCell>ID</TableCell>
                                        <TableCell>Username</TableCell>
                                        <TableCell>First Name</TableCell>
                                        <TableCell>Last Name</TableCell>
                                        <TableCell>Email</TableCell>
                                        <TableCell>Roles</TableCell>
                                    </TableRow>
                                </TableHead>
                                <TableBody>
                                    {users.map((user) => (
                                        <TableRow key={user.id}>
                                            <TableCell>{user.id}</TableCell>
                                            <TableCell>{user.userName}</TableCell>
                                            <TableCell>{user.firstName}</TableCell>
                                            <TableCell>{user.lastName}</TableCell>
                                            <TableCell>{user.email}</TableCell>
                                            <TableCell>{user.roles}</TableCell>
                                        </TableRow>
                                    ))}
                                </TableBody>
                            </Table>
                            <Pagination
                                count={totalPages}
                                page={page}
                                onChange={handlePageChange}
                                sx={{ mt: 2 }}
                            />
                        </>
                    )}
                </>
            ) : (
                // Prompt user to log in
                <Typography
                    sx={{
                        opacity: 0.5,
                        textAlign: "center",
                        mt: 5,
                        display: "flex",
                        alignItems: "center",
                        justifyContent: "center",
                    }}
                >
                    Login to See Users, click on the Menu App Drawer{" "}
                    <MenuIcon sx={{ margin: 1 }} /> to Login
                </Typography>
            )}
        </Box>
    );
};

export default HomePage;
