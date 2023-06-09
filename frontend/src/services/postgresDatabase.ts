import axios from "axios"

const postgresqlDatabase = axios.create({
  baseURL: "http://localhost:8080/api",
  timeout: 8000,
  headers: {
    "Content-Type": "application/json",
    "Access-Control-Allow-Origin": "http://localhost:3000",
    ...(localStorage.getItem("token") && {
      Authorization: `Bearer ${localStorage.getItem("token")}`,
    }),
  },
})
export default postgresqlDatabase