import axios, {AxiosInstance, AxiosResponse} from "axios"
import {AssignmentInterface} from "../types/AssignmentInterface";
import {SetStateAction} from "react";
import {AssignmentToSendInterface} from "../types/AssignmentToSendInterface";
import {AssignmentAddFilePropsInterface} from "../types/AssignmentAddFilePropsInterface";
import {FileFromPostInterface} from "../types/FileFromPostInterface";

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

export const postAssignmentWithUserAndGroupService = (
  userId: string | undefined,
  assignment: AssignmentToSendInterface,
  setIdAssignment: (arg0: number) => void,
  setToSend: (arg0: boolean) => void
) =>{
  postgresqlDatabase
    .post(
      `/assignment/withUserAndGroup/${localStorage.getItem("id")}/${userId}`,
      assignment
    )
    .then((r) => {
      setIdAssignment(r.data.id)
      setToSend(true)
    })
    .catch(error => console.log(error))
}

export const postFileWithAssignmentService = (
  response: AxiosResponse,
  props: AssignmentAddFilePropsInterface
) => {
  postgresqlDatabase
    .post(`file/withAssignment/${props.idAssigment}`, {
      mongo_id: response.data.id,
      format: response.data.format,
      name: response.data.name,
    })
    .then(() => props.setToNavigate(true))
}

export const getFilesByAssignmentService = (assigmentId: number): Promise<AxiosResponse> => {
  return postgresqlDatabase
    .get(
     `/files/byAssignment/${assigmentId}`
    )
}

export const putAssignmentService = (assignment: AssignmentInterface, setToSend: (arg0: boolean) => void) =>{
  postgresqlDatabase
    .put(`/assignment/${assignment.id}`, assignment)
    .then(() => setToSend(true))
    .catch((e) => console.log(e))
}l
export default postgresqlDatabase
