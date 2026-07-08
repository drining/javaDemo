import { createRouter, createWebHashHistory } from 'vue-router'
import DeptView from '../views/DeptView.vue'
import EmpView from '../views/EmpView.vue'

const router = createRouter({
  history: createWebHashHistory(),
  routes: [
    {
      path: '/',
      redirect: '/dept'
    },
    {
      path: '/dept',
      name: 'Dept',
      component: DeptView,
      meta: { title: '部门管理' }
    },
    {
      path: '/emp',
      name: 'Emp',
      component: EmpView,
      meta: { title: '员工管理' }
    }
  ]
})

export default router
