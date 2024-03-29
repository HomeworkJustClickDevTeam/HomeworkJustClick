import axios, { AxiosRequestConfig } from "axios"
import { getUser } from "./otherServices"

const mongoDatabaseServices = axios.create({
  baseURL: process.env.REACT_APP_MONGO_API_URL,
  timeout: 8082,
  headers: {
    "Content-Type": "multipart/form-data",
    "Access-Control-Allow-Origin": process.env.REACT_APP_CORS_URL
  },
})

mongoDatabaseServices.interceptors.request.use(
  async (config) => {
    const token = getUser()?.token
    if (token !== undefined) {
      config.headers["Authorization"] = "Bearer " + token
    } else {
      config.headers["Authorization"] = ""
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

mongoDatabaseServices.interceptors.response.use(
  (response) => {
    return response
  },
  async (error) => {
    const originalRequest = error.config
    if ((error.response?.status === 403 || error.code === "ERR_NETWORK") && !originalRequest._retry) {
      originalRequest._retry = true
      localStorage.removeItem("user")
      window.dispatchEvent(new Event("storage"))
      window.location.reload()
      return new Promise((resolve) => {
        resolve(mongoDatabaseServices(originalRequest))
      })
    }
    return Promise.reject(error)
  }
)

export const postFileMongoService = (file: FormData) => {
  return mongoDatabaseServices.post("file", file)
}

export const getFileMongoService = (
  mongoId: string,
  config?: AxiosRequestConfig
) => {
  return mongoDatabaseServices.get(`file/${mongoId}`, config)
}

export const deleteFileMongoService = (mongoId: string) => {
  return mongoDatabaseServices.delete(`file/${mongoId}`)
}
