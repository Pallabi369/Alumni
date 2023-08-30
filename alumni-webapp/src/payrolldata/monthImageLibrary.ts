import {DateTime} from "luxon";

const imgPerMonth = {
  'fallback': '',
  1: '/images/january.jpg',
  2: '/images/february.png',
  3: '/images/march.png',
  4: '/images/april.jpg',
  5: '/images/may.jpg',
  6: '/images/june.jpg',
  7: '/images/july.jpg',
  8: '/images/august.jpg',
  9: '/images/september.jpg',
  10: '/images/october.jpg',
  11: '/images/november.jpg',
  12: '/images/december.jpg'
}

export const getImageForDate = (value: DateTime | undefined) => {
  return value ? imgPerMonth[value.month] : imgPerMonth.fallback;
}
