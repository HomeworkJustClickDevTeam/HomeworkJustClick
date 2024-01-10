import { Table } from "../../types/Table.model"
import React, { useState } from "react"
import PointsTableForm from "./PointsTableForm"

export default function TablePointElement(props: {
  table: Table
  setEvaluationTable: (evaluationTable: Table[]) => void
  key: number | undefined
  tables: Table[] | undefined
}) {
  const [isEditFormHidden, setIsEditFormHidden] = useState<boolean>(true)
  const showPointsButton = () => {
    const points = props.table.buttons.map((button) => button.points)
    return points.join(", ")
  }

  const showEditForm = () => {
    setIsEditFormHidden(!isEditFormHidden)
  }

  return (
    <div className='border border-solid my-2 rounded-md max-w-[400px] pl-2 pb-1'>
      <h1 className='mt-1'>{props.table.name}</h1>
      <p>Punkty tabeli: <span className=''>[{showPointsButton()}]</span></p>
      <button onClick={() => showEditForm()} className=' underline text-main_blue'> EDYTUJ</button>
      {!isEditFormHidden && (
        <PointsTableForm
          table={props.table}
          setIsFormHidden={setIsEditFormHidden}
          setEvaluationTable={props.setEvaluationTable}
          tables={props.tables}
        />
      )}
    </div>
  )
}
