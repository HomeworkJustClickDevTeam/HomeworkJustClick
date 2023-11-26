import axios, { AxiosRequestConfig } from "axios"
import { getUser } from "./otherServices"

const mongoDatabaseServices = axios.create({
  baseURL: "http://homework_just_click_mongo_api:8082/api/",
  timeout: 8082,
  headers: {
    "Content-Type": `multipart/form-data`,
    "Access-Control-Allow-Origin": "http://localhost:3000",
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
    if (error.response.status === 403 && !originalRequest._retry) {
      originalRequest._retry = true
      localStorage.removeItem("user")
      window.dispatchEvent(new Event("storage"))
      window.location.reload()
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
