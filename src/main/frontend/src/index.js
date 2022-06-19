import React from 'react';
import App from './App';
import {createRoot} from "react-dom/client"
import app from "./App";

const root = createRoot(document.getElementById('root'));

root.render(
      <React.StrictMode>
      <App />
      </React.StrictMode>,
);

