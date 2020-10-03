import { gql } from '@apollo/client'

export const GET_TOPICS = gql`
  query Topic {
    topics {
      tag
    }
  }
`

// export const GET_TOPICS = gql`
//   query Topic {
//     topics {
//       tag
//       memes {
//         id
//         tags
//         createTime
//         url
//         counter {
//           likeCount
//           dislikeCount
//           dislikeCount
//           commentCount
//         }
//       }
//     }
//   }
// `
