const routes = [{
        path: '/',
        component: () =>
            import ('layouts/MainLayout.vue'),
        children: [{
                path: '',
                component: () =>
                    import ('pages/index.vue')
            }, {
                path: 'shopping',
                component: () =>
                    import ('pages/Shopping.vue')
            },
            {
                path: 'AddFlight',
                component: () =>
                    import ('pages/AddFlight.vue')
            }
        ]
    },

    // Always leave this as last one,
    // but you can also remove it
    {
        path: '/:catchAll(.*)*',
        component: () =>
            import ('pages/Error404.vue')
    }
]

export default routes