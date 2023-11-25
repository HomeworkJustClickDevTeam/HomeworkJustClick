import {cleanup} from "@testing-library/react";
import axios from "axios";
import {GroupInterface} from "../../types/GroupInterface";

jest.mock("axios")
afterEach(cleanup)

it("Gets sample groups", async () =>{
  (axios.get as jest.MockedFunction<typeof axios.get>).mockResolvedValue({data:[
    {id: 0,
      name: "test1",
      description: "opis Test 1",
      color: 0,
      archived: false},
      {id: 1,
        name: "test2",
        description: "opis Test 2",
        color: 1,
        archived: false}]})


})