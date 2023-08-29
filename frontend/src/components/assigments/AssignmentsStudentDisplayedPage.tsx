import React, {useContext, useEffect, useState} from "react"
import {getAssignmentsByStudentPostgresService} from "../../services/postgresDatabaseServices"
import AssigmentListElement from "./AssigmentListElement"
import {AssignmentInterface} from "../../types/AssignmentInterface";
import {getUser} from "../../services/otherServices";
import HomePage from "../home/HomePage";
import {useNavigate} from "react-router-dom";

export default function AssignmentsStudentDisplayedPage() {
  const [assignments, setAssignments] = useState<AssignmentInterface[]>([])
  const userState = getUser()
  const navigate = useNavigate()

  useEffect(() => {
    if(userState !== undefined) {
      getAssignmentsByStudentPostgresService(userState.id.toString())
        .then((response) => {
          const assigmentFromServer = response.data as AssignmentInterface[]
          setAssignments(assigmentFromServer)
        })
    }
  }, [])
  if(userState === undefined){
    navigate("/")
  }
  return (
    <div>
      <ul>
        {assignments.map((assigment) => (
          <li key={assigment.id}>
            <AssigmentListElement
              assignment={assigment}
              idGroup={`${assigment.groupId}`}
            />
          </li>
        ))}
      </ul>
    </div>
  )
}
