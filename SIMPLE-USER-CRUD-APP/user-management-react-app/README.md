# Login Form with Authentication

This project is a React-based login form that integrates with a backend service for authentication. It uses Material-UI components for a modern and responsive user interface. The project demonstrates client-side form validation, API integration, and token-based authentication.

## Features

- **User Login**: Authenticate users with username and password.
- **Token Management**: Store JWT tokens in `localStorage` for session persistence.
- **Error Handling**: Display error messages for failed login attempts using Material-UI's `Snackbar` and `Alert` components.
- **Navigation**: Redirect users to a home page upon successful login.

## Technologies Used

- **React**: Frontend framework for building user interfaces.
- **Material-UI**: Library for UI components and styling.
- **React Router**: For handling navigation between pages.
- **Custom UserService**: To handle API interactions.

## Prerequisites

- Node.js (>=14.x)
- npm or yarn

## Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/your-repository.git
   ```
2. Navigate to the project directory:
   ```bash
   cd user-management-react-app/
   ```
3. Install dependencies:
   ```bash
   npm install
   ```

## Usage

1. Start the development server:
   ```bash
   npm start
   ```
2. Open your browser and navigate to:
   ```
   http://localhost:3000
   ```

## Project Structure

```
.
├── public
│   ├── index.html      # Main HTML file
│   └── ...
├── src
│   ├── components
│   │   ├── LoginForm.js # Login form component
│   │   └── ...
│   ├── service
│   │   └── UserService.js # API interactions
│   ├── App.js          # Main application component
│   ├── index.js        # Entry point for React
│   └── ...
└── package.json        # Project metadata and dependencies
```

## Components

### `LoginForm`

- **Props**:
  - `setIsAuthenticated`: Function to update the authentication state.
- **State**:
  - `credentials`: Object containing `userName` and `passWord`.
  - `error`: String to store error messages.
- **Functionality**:
  - Captures user input.
  - Sends login credentials to the backend.
  - Displays error messages for failed logins.

### `UserService`

- Handles API interactions for user authentication.
- Example login method:
  ```javascript
  async (credentials) => {
    try {
      const response = await axios.post(`${API_URL}generateToken`, credentials);
      return response.data;
    } catch (error) {
      console.error("Error during login:", error);
      throw error;
    }
  };
  ```

## Environment Variables

Ensure the backend URL is set in an environment variable or hardcoded during development. For example:

```env
REACT_APP_API_URL=http://localhost:8080
```

## Deployment

1. Build the project for production:
   ```bash
   npm run build
   ```
2. Deploy the contents of the `build` directory to your web server or hosting provider.

## Contributing

1. Fork the repository.
2. Create a new branch for your feature:
   ```bash
   git checkout -b feature-name
   ```
3. Commit your changes:
   ```bash
   git commit -m "Description of changes"
   ```
4. Push your branch:
   ```bash
   git push origin feature-name
   ```
5. Open a pull request.

## License

This project is licensed under the MIT License. See the LICENSE file for details.

## Contact

For questions or suggestions, please contact:

- **Author Name**: PUSHKAR D
- **Email**: pushkardwarkanath@gmail.com
- **GitHub**: [pushkar666](https://github.com/pushkar666)

---

Thank you for checking out this project! Feel free to contribute or reach out for collaborations.
