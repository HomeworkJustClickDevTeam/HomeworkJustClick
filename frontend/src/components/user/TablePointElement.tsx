import { Table } from "../../types/Table.model"
import React, { useState } from "react"
import PointsTableForm from "./PointsTableForm"

export default function TablePointElement(props: {
  table: Table
  key: number | undefined
}) {
  const [isEditFormHidden, setIsEditFormHidden] = useState<boolean>(true)
  const showPointsButton = () => {
    const points = props.table.buttons.map((button) => button.points)
    return points.join(", ")
  }
  const showEditForm = () => {
    setIsEditFormHidden(false)
  }
  return (
    <div>
      <h1>{props.table.name}</h1>
      <p>({showPointsButton()})</p>
      <button onClick={() => showEditForm()}> EDIT</button>
      {!isEditFormHidden && (
        <PointsTableForm
          table={props.table}
          setIsFormHidden={setIsEditFormHidden}
        />
      )}
    </div>
  )
}
