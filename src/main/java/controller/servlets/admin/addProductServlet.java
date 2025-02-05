package controller.servlets.admin;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import controller.database.DatabaseController;
import model.ProductModel;
import utils.StringUtils;

/**
 * Servlet implementation class addProduct
 */
@WebServlet("/addProductServlet")
@MultipartConfig(fileSizeThreshold =1024*1024*2, //2MB
maxFileSize=1024*1024*10,//10MB,
maxRequestSize=1024*1024*50)
public class addProductServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private DatabaseController dbController = new DatabaseController();
	
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public addProductServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String contentType = request.getContentType();
		 System.out.println("Content Type: " + contentType);
		String productName = request.getParameter("name");
		String productDescription = request.getParameter("description");
		int productId = Integer.parseInt(request.getParameter("Id"));
		float productPrice =Float.parseFloat(request.getParameter("price"));
		int productStock = Integer.parseInt(request.getParameter("stock"));
		
		Part partImage = request.getPart("images");
 		//String fileName = getFileName(part);
		System.out.println(partImage);		
		ProductModel productModel = new ProductModel(productName,productDescription,productPrice,productStock,productId,partImage);
 				
 		// Save the uploaded file to the server
 		String savePath = StringUtils.IMAGE_DIR_SAVE_PATH;
 		String fileName = productModel.getImageURLfromPart();
 		
 		System.out.println(fileName);
 		if(!fileName.isEmpty() && fileName != null) {
 			partImage.write(savePath+fileName);
 		}

 		
 		boolean existingProduct = dbController.productExists(productId);
 		
 		if(existingProduct)
 		{
 			//Product already exists, display error message
 			request.setAttribute("registerMessage", "Product exists!!");
 			request.getRequestDispatcher(StringUtils.addPRODUCT_PAGE).forward(request, response);
        }
 		else
 		{
 			//Product does not exist, add new product
 			dbController.addProduct(productModel);
 			request.setAttribute("registerMessage", "Registered SuccessFully!!!!");
 			request.getRequestDispatcher(StringUtils.addPRODUCT_PAGE).forward(request, response);
 			
 		}
 		
	}
}


