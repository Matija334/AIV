import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './App';
import reportWebVitals from './reportWebVitals';
import {createBrowserRouter, RouterProvider} from "react-router-dom";
import ErrorPage from "./Components/ErrorPage/ErrorPage";
import AllPatients from "./Components/Patients/AllPatients";
import PatientDetails from "./Components/Patients/PatientDetails";

const root = ReactDOM.createRoot(
  document.getElementById('root') as HTMLElement
);

const router = createBrowserRouter([
    {
        path: '/',
        element: <App/>,
        errorElement: <ErrorPage />,
        children: [
            {
                path: '/patientList',
                element: <AllPatients/>,
                errorElement: <ErrorPage/>,
            },
            {
                path: '/patientList/:patientEmail',
                element: <PatientDetails />,
                errorElement: <ErrorPage/>,
            },
        ],
    },
    {
        path: '*',
        element: <ErrorPage/>,
        errorElement: <ErrorPage/>,
    },
]);
root.render(
    <React.StrictMode>
        <RouterProvider router={router}/>
    </React.StrictMode>
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
