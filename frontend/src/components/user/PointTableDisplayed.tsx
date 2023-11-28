import { Table } from "../../types/Table.model"
import React from "react"
import TablePointElement from "./TablePointElement"

export const PointTableDisplayed = (props: {
  tables: Table[] | undefined
  setEvaluationTable: (evaluationTable: Table[]) => void
}) => {
  return (
    <div>
      {props.tables?.map(
        (table) =>
          props.tables &&
          props.tables?.length > 0 && (
            <TablePointElement
              table={table}
              key={table.id}
              setEvaluationTable={props.setEvaluationTable}
              tables={props.tables}
            />
          )
      )}
    </div>
  )
}
