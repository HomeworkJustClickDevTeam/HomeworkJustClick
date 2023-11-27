import React from "react"
import ReactDOM from "react-dom/client"
// import './assets/index.css';
import "./index.css"
import App from "./App"
import reportWebVitals from "./reportWebVitals"
import { BrowserRouter } from "react-router-dom"
import { Provider } from "react-redux"
import {AppStore, setupStore} from "./redux/store"
import {getUser} from "./services/otherServices";

const root = ReactDOM.createRoot(
  document.getElementById('root') as HTMLElement
);
root.render(
  <Provider store={setupStore({userState: getUser()})}>
    <React.StrictMode>
        <BrowserRouter>
          <App/>
        </BrowserRouter>
    </React.StrictMode>
  </Provider>
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
