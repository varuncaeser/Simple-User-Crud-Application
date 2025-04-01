import axios from 'axios';
// import { data } from 'react-router-dom';

const API_URL = 'http://localhost:8080/auth/'; // Replace with your actual API base URL

const UserService = {
    /**
     * Fetches users with pagination from the server.
     * 
     * This function sends a GET request to the server's 'users' endpoint with pagination details.
     * It uses the axios library to make the HTTP request and handles any errors that may occur during the process.
     * 
     * @param {number} [page=0] - The page number for pagination. Default is 0.
     * @param {number} [size=10] - The number of records per page. Default is 10.
     * 
     * @returns {Promise} - A Promise that resolves with the server's response data (if the request is successful) or rejects with an error (if the request fails).
     * 
     * @throws {Error} - Throws an error if the request fails or if the token is missing.
     * 
     * @example
     * try {
     *     const response = await UserService.getUsers(1, 20);
     *     console.log('Fetched users:', response);
     * } catch (error) {
     *     console.error('Error fetching users:', error);
     * }
     */
    getUsers: async (page = 0, size = 10) => {
        try {
            const token = localStorage.getItem('token');
            // console.log(token);
            if (!token) {
                throw new Error('Token is missing. Please log in again.');
            }

            const config = {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
                params: {
                    page: page,
                    size: size
                },
            };
            // console.log(config);
            const response = await axios.get(`${API_URL}users`, config);
            // console.log(response.data);

            return response.data;
        } catch (error) {
            console.error('Error fetching users:', error);
            throw error;
        }
    },


    // queryUsers: async (queryParams, page = 0, size = 10) => {
    //     try {
    //         const token = localStorage.getItem('token');
    //         const config = {
    //             headers: {
    //                 Authorization: `Bearer ${token}`,
    //             },
    //             params: {
    //                 page: page,
    //                 size: size
    //             },
    //         };
    //         const response = await axios.post(`${API_URL}queryUsers`, queryParams, config);
    //         return response.data;
    //     } catch (error) {
    //         console.error('Error fetching queried users:', error);
    //         throw error;
    //     }
    // }
    /**
     * Fetches users based on the provided query parameters with pagination.
     * 
     * This function sends a POST request to the server's 'queryUsers' endpoint with the provided query parameters and pagination details.
     * It uses the axios library to make the HTTP request and handles any errors that may occur during the process.
     * 
     * @param {Object} queryParams - An object containing the query parameters.
     * @param {number} [page=0] - The page number for pagination. Default is 0.
     * @param {number} [size=10] - The number of records per page. Default is 10.
     * 
     * @returns {Promise} - A Promise that resolves with the server's response data (if the request is successful) or rejects with an error (if the request fails).
     * 
     * @throws {Error} - Throws an error if the request fails or if the token is missing.
     * 
     * @example
     * const queryParams = {
     *     firstName: 'John',
     *     lastName: 'Doe',
     *     email: 'johndoe@example.com'
     * };
     * 
     * try {
     *     const response = await UserService.queryUsers(queryParams, 1, 20);
     *     console.log('Queried users:', response);
     * } catch (error) {
     *     console.error('Error fetching queried users:', error);
     * }
     */
    queryUsers: async (queryParams, page = 0, size = 10) => {
        try {
            console.log(queryParams);
            const token = localStorage.getItem('token');
            const config = {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            };
            const body = {
                ...queryParams,
                page: page,
                size: size
            };
            const response = await axios.post(`${API_URL}queryUsers`, body, config); // Using POST and sending data in the body
            console.log(response);
            return response.data;
        } catch (error) {
            console.error('Error fetching queried users:', error);
            throw error;
        }
    },

    /**
     * Authenticates the user and fetches a token.
     * 
     * This function sends a POST request to the server's 'generateToken' endpoint with the provided user credentials.
     * It uses the axios library to make the HTTP request and handles any errors that may occur during the process.
     *
     * @param {Object} credentials - User credentials { userName, passWord }.
     * @param {string} credentials.userName - The username of the user.
     * @param {string} credentials.passWord - The password of the user.
     * 
     * @returns {Promise} - A Promise that resolves with the server's response data (if the request is successful) or rejects with an error (if the request fails).
     * 
     * @throws {Error} - Throws an error if the request fails.
     * 
     * @example
     * const userCredentials = {
     *     userName: 'johndoe',
     *     passWord: 'P@ssw0rd'
     * };
     * 
     * try {
     *     const response = await UserService.login(userCredentials);
     *     console.log('Logged in successfully:', response);
     * } catch (error) {
     *     console.error('Error during login:', error);
     * }
     */
    login: async (credentials) => {
        try {
            const response = await axios.post(`${API_URL}generateToken`, credentials);
            return response.data;
        } catch (error) {
            console.error('Error during login:', error);
            throw error;
        }
    },

    /**
     * Adds a new user to the system.
     * 
     * This function sends a POST request to the server's 'addNewUser' endpoint with the provided user information.
     * It uses the axios library to make the HTTP request and handles any errors that may occur during the process.
     * 
     * @param {Object} userInfo - An object containing the user's information.
     * @param {string} userInfo.username - The username of the new user.
     * @param {string} userInfo.firstName - The first name of the new user.
     * @param {string} userInfo.lastName - The last name of the new user.
     * @param {string} userInfo.email - The email of the new user.
     * @param {string} userInfo.password - The password of the new user.
     * 
     * @returns {Promise} - A Promise that resolves with the server's response data (if the request is successful) or rejects with an error (if the request fails).
     * 
     * @throws {Error} - Throws an error if the request fails.
     * 
     * @example
     * const newUser = {
     *     username: 'newUser123',
     *     firstName: 'John',
     *     lastName: 'Doe',
     *     email: 'johndoe@example.com',
     *     password: 'P@ssw0rd'
     * };
     * 
     * try {
     *     const response = await UserService.addNewUser(newUser);
     *     console.log('New user added successfully:', response);
     * } catch (error) {
     *     console.error('Error adding new user:', error);
     * }
     */
    addNewUser: async (userInfo) => {
        try {
            const response = await axios.post(`${API_URL}addNewUser`, userInfo);
            return response.data;
        } catch (error) {
            console.error('Error adding new user:', error);
            throw error;
        }
    },

    /**
     * Performs a logout operation by sending a POST request to the server.
     * It retrieves the token from local storage and includes it in the request headers for authentication.
     * If the token is missing, it throws an error.
     * 
     * @returns {Promise} - Returns a Promise that resolves with the server response data or rejects with an error.
     * 
     * @throws {Error} - Throws an error if the token is missing.
     * 
     * @example
     * try {
     *     const response = await UserService.logout();
     *     console.log('Logged out successfully:', response);
     * } catch (error) {
     *     console.error('Error during logout:', error);
     * }
     */
    logout: async () => {
        try {
            const token = localStorage.getItem('token');
            if (!token) {
                throw new Error('Token is missing. Please log in again.');
            }

            const config = {
                headers: {
                    Authorization: `Bearer ${token}`,
                }
            };
            const response = await axios.post(`${API_URL}logout`, {}, config);
            return response.data;
        }
        catch (error) {
            console.error('Error during logout:', error);
            throw error;
        }
    },

};

export default UserService;
