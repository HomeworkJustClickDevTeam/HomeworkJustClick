import React, {useContext, useEffect, useState} from "react"
import {getAssignmentsByStudentPostgresService} from "../../services/postgresDatabaseServices"
import AssigmentListElement from "./AssigmentListElement"
import {AssignmentInterface} from "../../types/AssignmentInterface";
import {getUser} from "../../services/otherServices";

export default function AssignmentsStudentDisplayedPage() {
  const [assignments, setAssignments] = useState<AssignmentInterface[]>([])
  const userState = getUser()

  useEffect(() => {
    if(userState !== undefined) {
      getAssignmentsByStudentPostgresService(userState.id.toString())
        .then((response) => {
          const assigmentFromServer = response.data as AssignmentInterface[]
          console.log('assigmner', assigmentFromServer)
          setAssignments(assigmentFromServer)
        })
    }
  }, [])
  if(userState === undefined){
    return <>Not logged in</>
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
