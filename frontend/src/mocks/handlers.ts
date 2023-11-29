import { http, HttpResponse, delay } from "msw";
import { be } from "date-fns/locale";


export const homePageHandlers = [
  http.get('http://localhost:8080/api/groups/byTeacher/:id', async ({params}) => {
    await delay(100)
    if(params.id !== "undefined" && params.id !== "null" && params.id === '0'){
      return HttpResponse.json(null, {status: 404})
    }
    return HttpResponse.json([
        {
          id: 0,
          name: "Example Group 1",
          description: "Example desc",
          color: 0,
          archived: false
        },
        {
          id: 2,
          name: "Example Group 2",
          description: "Example desc 2",
          color: 1,
          archived: false
        }
      ],
      {
        status: 200
      })
  }),
  http.get('http://localhost:8080/api/groups/byStudent/:id', async ({params}) => {
    await delay(100)
    return HttpResponse.json(null, {status: 404})
  }),
  http.get('http://localhost:8080/api/groups/byUser/:id', async ({params}) => {
    const {id} = params
    await delay(100)
    if(params.id !== "undefined" && params.id !== "null" && params.id === '0'){
      return HttpResponse.json(null, {status: 404})
    }
    return HttpResponse.json( [
          {
            id: 0,
            name: "Example Group 1",
            description: "Example desc",
            color: 0,
            archived: false
          },
          {
            id: 2,
            name: "Example Group 2",
            description: "Example desc 2",
            color: 1,
            archived: false
          }
        ],
      {
        status: 200
      })
  }),
  http.get('http://localhost:8080/api/groups/userCheckWithRole/:userId/:groupId', async ({params}) => {
    const {userId, groupId} = params
    await delay(100)
    if(userId === '3'){
      return new Response("Teacher")
    }
    else
      return new Response("Student")
  })
]

export const loginPageHandler = [
  http.post('http://localhost:8080/api/auth/authenticate', async () => {

    await delay(100)
    return HttpResponse.json({
        token: "eyJhbGc3ZGEuY29tIiwiaWF0IjoxNzAwOTkwNjkyLCJleHAiOjE3MDA5OTQyOTJ9.B22HfZ1nIWfmHB66lt0vppGE86eERfTM-f61GG--v0LB-7dulmb4H11Q3niO4NYSJncEE5JzRhoBHhdraIP3og",
        id: 1,
        role: "USER",
        message: "ok",
        color: 0,
        name: "Jan",
        lastname: "Kowalski",
        index: 123123
      }, {
      status: 200
    })
  })
]